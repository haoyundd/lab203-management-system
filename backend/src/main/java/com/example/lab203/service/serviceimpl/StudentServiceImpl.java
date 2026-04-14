package com.example.lab203.service.serviceimpl;

import com.example.lab203.common.exception.BizException;
import com.example.lab203.common.result.PageResult;
import com.example.lab203.common.util.AvatarSvgGenerator;
import com.example.lab203.common.util.AuthAssert;
import com.example.lab203.common.util.BeanCopyUtils;
import com.example.lab203.dto.StudentQueryDTO;
import com.example.lab203.dto.StudentSaveDTO;
import com.example.lab203.entity.AccountEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.AccountManager;
import com.example.lab203.manager.StudentManager;
import com.example.lab203.service.StudentService;
import com.example.lab203.vo.StudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 学生服务实现。
 */
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    /**
     * 头像渐变主色集合，按学号哈希稳定分配，避免每次生成风格漂移。
     */
    private static final List<String> AVATAR_COLORS = Arrays.asList(
            "#0F7CFF", "#19A974", "#F59E0B", "#EF4444", "#0EA5E9",
            "#14B8A6", "#E11D48", "#84CC16", "#F97316", "#2563EB"
    );

    private final StudentManager studentManager;
    private final AccountManager accountManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.avatar.storage-dir}")
    private String avatarStorageDir;

    public StudentServiceImpl(StudentManager studentManager, AccountManager accountManager, PasswordEncoder passwordEncoder) {
        this.studentManager = studentManager;
        this.accountManager = accountManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 分页查询学生。
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<StudentVO> page(StudentQueryDTO queryDTO) {
        AuthAssert.requireAdmin();
        List<StudentEntity> filtered = filterStudents(queryDTO.getKeyword());
        int pageNo = Math.max(queryDTO.getPageNo(), 1);
        int pageSize = Math.max(queryDTO.getPageSize(), 1);
        int fromIndex = Math.min((pageNo - 1) * pageSize, filtered.size());
        int toIndex = Math.min(fromIndex + pageSize, filtered.size());
        List<StudentVO> records = BeanCopyUtils.copyList(filtered.subList(fromIndex, toIndex), StudentVO.class);
        return new PageResult<>((long) filtered.size(), records);
    }

    /**
     * 获取学生详情。
     *
     * @param id 主键
     * @return 学生详情
     */
    @Override
    public StudentVO detail(Long id) {
        AuthAssert.requireLogin();
        StudentEntity studentEntity = requireStudent(id);
        return BeanCopyUtils.copy(studentEntity, StudentVO.class);
    }

    /**
     * 保存学生。
     *
     * @param saveDTO 保存对象
     * @return 学生详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentVO save(StudentSaveDTO saveDTO) {
        AuthAssert.requireAdmin();
        StudentEntity duplicateStudent = studentManager.getByStudentNo(saveDTO.getStudentNo());
        if (duplicateStudent != null && !duplicateStudent.getId().equals(saveDTO.getId())) {
            throw new BizException(400, "学号已存在");
        }
        StudentEntity studentEntity = saveDTO.getId() == null ? new StudentEntity() : requireStudent(saveDTO.getId());
        if (studentEntity.getLatestResearchHours() == null) {
            studentEntity.setLatestResearchHours(BigDecimal.ZERO.setScale(1));
        }
        if (studentEntity.getTotalResearchHours() == null) {
            studentEntity.setTotalResearchHours(BigDecimal.ZERO.setScale(1));
        }
        studentEntity.setStudentNo(saveDTO.getStudentNo());
        studentEntity.setName(saveDTO.getName());
        studentEntity.setGrade(saveDTO.getGrade());
        studentEntity.setMajor(saveDTO.getMajor());
        studentEntity.setResearchDirection(saveDTO.getResearchDirection());
        studentEntity.setPhone(saveDTO.getPhone());
        studentEntity.setEmail(saveDTO.getEmail());
        if (studentEntity.getId() == null) {
            studentEntity.setIsDeleted(0);
            studentEntity.setCreateTime(LocalDateTime.now());
        }
        studentEntity.setAvatarPath(resolveAvatarPath(studentEntity, saveDTO));
        studentEntity.setUpdateTime(LocalDateTime.now());
        studentManager.save(studentEntity);
        // 新增学生时自动创建账号，默认用户名为学号，默认密码为 Lab203@123。
        if (saveDTO.getId() == null && accountManager.getByUsername(studentEntity.getStudentNo()) == null) {
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setUsername(studentEntity.getStudentNo());
            accountEntity.setPasswordHash(passwordEncoder.encode("Lab203@123"));
            accountEntity.setRole("STUDENT");
            accountEntity.setStudentId(studentEntity.getId());
            accountEntity.setDisplayName(studentEntity.getName());
            accountEntity.setIsDeleted(0);
            accountEntity.setCreateTime(LocalDateTime.now());
            accountEntity.setUpdateTime(LocalDateTime.now());
            accountManager.save(accountEntity);
        }
        log.info("[StudentService] 保存学生: {}", studentEntity.getStudentNo());
        return BeanCopyUtils.copy(studentEntity, StudentVO.class);
    }

    /**
     * 删除学生。
     *
     * @param id 主键
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AuthAssert.requireAdmin();
        requireStudent(id);
        studentManager.delete(id);
        log.info("[StudentService] 删除学生: {}", id);
    }

    /**
     * 查询全部学生。
     *
     * @return 学生列表
     */
    @Override
    public List<StudentVO> listAll() {
        AuthAssert.requireAdmin();
        return BeanCopyUtils.copyList(studentManager.listAll(), StudentVO.class);
    }

    /**
     * 校验并获取学生。
     *
     * @param id 主键
     * @return 学生实体
     */
    private StudentEntity requireStudent(Long id) {
        StudentEntity studentEntity = studentManager.getById(id);
        if (studentEntity == null) {
            throw new BizException(404, "学生不存在");
        }
        return studentEntity;
    }

    /**
     * 根据关键字筛选学生。
     *
     * @param keyword 关键字
     * @return 学生列表
     */
    private List<StudentEntity> filterStudents(String keyword) {
        return studentManager.listAll().stream()
                .filter(student -> !StringUtils.hasText(keyword)
                        || student.getName().contains(keyword)
                        || student.getStudentNo().contains(keyword)
                        || student.getResearchDirection().contains(keyword))
                .sorted(Comparator.comparing(StudentEntity::getId))
                .toList();
    }

    /**
     * 解析学生头像路径。
     * 创建学生时如果没有传头像，则自动生成 SVG 头像并写入磁盘；
     * 修改学生时如果未传新头像，则保留原头像路径，避免误清空。
     *
     * @param studentEntity 学生实体
     * @param saveDTO 保存参数
     * @return 最终头像访问路径
     */
    private String resolveAvatarPath(StudentEntity studentEntity, StudentSaveDTO saveDTO) {
        if (StringUtils.hasText(saveDTO.getAvatarPath())) {
            return saveDTO.getAvatarPath();
        }
        if (StringUtils.hasText(studentEntity.getAvatarPath())) {
            return studentEntity.getAvatarPath();
        }
        return generateAvatar(studentEntity.getStudentNo(), studentEntity.getName());
    }

    /**
     * 自动生成学生 SVG 头像，并返回前端可访问路径。
     *
     * @param studentNo 学号
     * @param name 姓名
     * @return 头像访问路径
     */
    private String generateAvatar(String studentNo, String name) {
        String safeStudentNo = studentNo.replaceAll("[^0-9A-Za-z_-]", "");
        int colorIndex = Math.abs(studentNo.hashCode()) % AVATAR_COLORS.size();
        String primaryColor = AVATAR_COLORS.get(colorIndex);
        String secondaryColor = AVATAR_COLORS.get((colorIndex + 3) % AVATAR_COLORS.size());
        String displayText = buildAvatarDisplayText(studentNo, name);
        Path outputPath = Path.of(avatarStorageDir, "student-" + safeStudentNo + ".svg");
        try {
            AvatarSvgGenerator.generate(outputPath, displayText, primaryColor, secondaryColor);
            return "/uploads/avatars/student-" + safeStudentNo + ".svg";
        } catch (IOException exception) {
            log.error("[StudentService] 自动生成头像失败: studentNo={}", studentNo, exception);
            throw new BizException(500, "学生头像生成失败，请稍后重试");
        }
    }

    /**
     * 生成头像展示文案：优先展示姓氏加学号尾号，兼顾辨识度与稳定性。
     *
     * @param studentNo 学号
     * @param name 姓名
     * @return 头像显示文字
     */
    private String buildAvatarDisplayText(String studentNo, String name) {
        String prefix = StringUtils.hasText(name) ? name.substring(0, 1) : "生";
        String suffix = studentNo.length() > 2 ? studentNo.substring(studentNo.length() - 2) : studentNo;
        return prefix + suffix;
    }
}
