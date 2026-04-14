package com.example.lab203.service.serviceimpl;

import com.example.lab203.common.exception.BizException;
import com.example.lab203.common.util.AuthAssert;
import com.example.lab203.common.util.RandomResearchTimeGenerator;
import com.example.lab203.entity.ResearchTimeRecordEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.ResearchTimeRecordManager;
import com.example.lab203.manager.StudentManager;
import com.example.lab203.service.ResearchTimeRecordService;
import com.example.lab203.vo.CurrentUserVO;
import com.example.lab203.vo.ResearchTimeRecordVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 科研时长服务实现。
 */
@Service
public class ResearchTimeRecordServiceImpl implements ResearchTimeRecordService {

    private final ResearchTimeRecordManager researchTimeRecordManager;
    private final StudentManager studentManager;

    public ResearchTimeRecordServiceImpl(ResearchTimeRecordManager researchTimeRecordManager, StudentManager studentManager) {
        this.researchTimeRecordManager = researchTimeRecordManager;
        this.studentManager = studentManager;
    }

    /**
     * 生成今日科研时长。
     *
     * @return 记录详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResearchTimeRecordVO generateToday() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        if (currentUser.getStudentId() == null) {
            throw new BizException(400, "当前账号不是学生账号");
        }
        LocalDate today = LocalDate.now();
        ResearchTimeRecordEntity existed = researchTimeRecordManager.getByStudentAndDate(currentUser.getStudentId(), today);
        if (existed != null) {
            throw new BizException(400, "今天已经生成过科研时长了");
        }
        StudentEntity studentEntity = requireStudent(currentUser.getStudentId());
        BigDecimal hours = RandomResearchTimeGenerator.generate(currentUser.getStudentId(), today);
        ResearchTimeRecordEntity entity = new ResearchTimeRecordEntity();
        entity.setStudentId(currentUser.getStudentId());
        entity.setRecordDate(today);
        entity.setHours(hours);
        entity.setSourceType("SYSTEM_RANDOM");
        entity.setIsDeleted(0);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        researchTimeRecordManager.save(entity);
        studentEntity.setLatestResearchHours(hours);
        studentEntity.setTotalResearchHours(studentEntity.getTotalResearchHours().add(hours));
        studentEntity.setUpdateTime(LocalDateTime.now());
        studentManager.save(studentEntity);
        return toVO(entity, studentEntity);
    }

    /**
     * 查询我的科研时长历史。
     *
     * @return 时长列表
     */
    @Override
    public List<ResearchTimeRecordVO> myHistory() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        if (currentUser.getStudentId() == null) {
            throw new BizException(400, "当前账号不是学生账号");
        }
        StudentEntity studentEntity = requireStudent(currentUser.getStudentId());
        return researchTimeRecordManager.listAll().stream()
                .filter(record -> currentUser.getStudentId().equals(record.getStudentId()))
                .sorted(Comparator.comparing(ResearchTimeRecordEntity::getRecordDate).reversed())
                .map(record -> toVO(record, studentEntity))
                .toList();
    }

    /**
     * 查询全部科研时长记录。
     *
     * @return 时长列表
     */
    @Override
    public List<ResearchTimeRecordVO> listAll() {
        AuthAssert.requireAdmin();
        Map<Long, StudentEntity> studentMap = studentManager.listAll().stream().collect(Collectors.toMap(StudentEntity::getId, Function.identity()));
        return researchTimeRecordManager.listAll().stream()
                .sorted(Comparator.comparing(ResearchTimeRecordEntity::getRecordDate).reversed())
                .map(record -> toVO(record, studentMap.get(record.getStudentId())))
                .toList();
    }

    /**
     * 校验学生是否存在。
     *
     * @param studentId 学生主键
     * @return 学生实体
     */
    private StudentEntity requireStudent(Long studentId) {
        StudentEntity studentEntity = studentManager.getById(studentId);
        if (studentEntity == null) {
            throw new BizException(404, "学生不存在");
        }
        if (studentEntity.getLatestResearchHours() == null) {
            studentEntity.setLatestResearchHours(BigDecimal.ZERO.setScale(1));
        }
        if (studentEntity.getTotalResearchHours() == null) {
            studentEntity.setTotalResearchHours(BigDecimal.ZERO.setScale(1));
        }
        return studentEntity;
    }

    /**
     * 转换科研时长视图对象。
     *
     * @param entity 时长实体
     * @param studentEntity 学生实体
     * @return 视图对象
     */
    private ResearchTimeRecordVO toVO(ResearchTimeRecordEntity entity, StudentEntity studentEntity) {
        ResearchTimeRecordVO vo = new ResearchTimeRecordVO();
        vo.setId(entity.getId());
        vo.setStudentId(entity.getStudentId());
        vo.setStudentName(studentEntity == null ? "未知学生" : studentEntity.getName());
        vo.setRecordDate(entity.getRecordDate());
        vo.setHours(entity.getHours());
        vo.setSourceType(entity.getSourceType());
        return vo;
    }
}
