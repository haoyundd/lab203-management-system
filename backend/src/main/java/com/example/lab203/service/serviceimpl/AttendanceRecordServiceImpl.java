package com.example.lab203.service.serviceimpl;

import com.example.lab203.common.enums.AttendanceStatusEnum;
import com.example.lab203.common.exception.BizException;
import com.example.lab203.common.util.AuthAssert;
import com.example.lab203.dto.AttendanceRecordQueryDTO;
import com.example.lab203.dto.AttendanceRecordSaveDTO;
import com.example.lab203.entity.AttendanceRecordEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.AttendanceRecordManager;
import com.example.lab203.manager.StudentManager;
import com.example.lab203.service.AttendanceRecordService;
import com.example.lab203.vo.AttendanceRecordVO;
import com.example.lab203.vo.CurrentUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 出勤服务实现。
 */
@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    private final AttendanceRecordManager attendanceRecordManager;
    private final StudentManager studentManager;

    public AttendanceRecordServiceImpl(AttendanceRecordManager attendanceRecordManager, StudentManager studentManager) {
        this.attendanceRecordManager = attendanceRecordManager;
        this.studentManager = studentManager;
    }

    /**
     * 学生签到。
     *
     * @return 今日记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttendanceRecordVO signIn() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        if (currentUser.getStudentId() == null) {
            throw new BizException(400, "当前账号不是学生账号");
        }
        LocalDate today = LocalDate.now();
        AttendanceRecordEntity recordEntity = attendanceRecordManager.getByStudentAndDate(currentUser.getStudentId(), today);
        if (recordEntity != null && recordEntity.getSignInTime() != null) {
            throw new BizException(400, "今天已经签到过了");
        }
        if (recordEntity == null) {
            recordEntity = new AttendanceRecordEntity();
            recordEntity.setStudentId(currentUser.getStudentId());
            recordEntity.setAttendanceDate(today);
            recordEntity.setIsDeleted(0);
            recordEntity.setCreateTime(LocalDateTime.now());
        }
        recordEntity.setSignInTime(LocalDateTime.now());
        recordEntity.setStatus(AttendanceStatusEnum.SIGNED_IN.name());
        recordEntity.setUpdateTime(LocalDateTime.now());
        attendanceRecordManager.save(recordEntity);
        return toVO(recordEntity, studentManager.getById(currentUser.getStudentId()));
    }

    /**
     * 学生签退。
     *
     * @return 今日记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttendanceRecordVO signOut() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        if (currentUser.getStudentId() == null) {
            throw new BizException(400, "当前账号不是学生账号");
        }
        AttendanceRecordEntity recordEntity = attendanceRecordManager.getByStudentAndDate(currentUser.getStudentId(), LocalDate.now());
        if (recordEntity == null || recordEntity.getSignInTime() == null) {
            throw new BizException(400, "今天尚未签到，无法签退");
        }
        if (recordEntity.getSignOutTime() != null) {
            throw new BizException(400, "今天已经签退过了");
        }
        recordEntity.setSignOutTime(LocalDateTime.now());
        recordEntity.setStatus(AttendanceStatusEnum.COMPLETED.name());
        recordEntity.setUpdateTime(LocalDateTime.now());
        attendanceRecordManager.save(recordEntity);
        return toVO(recordEntity, studentManager.getById(currentUser.getStudentId()));
    }

    /**
     * 管理员保存出勤。
     *
     * @param saveDTO 保存对象
     * @return 出勤详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttendanceRecordVO saveByAdmin(AttendanceRecordSaveDTO saveDTO) {
        AuthAssert.requireAdmin();
        StudentEntity studentEntity = requireStudent(saveDTO.getStudentId());
        AttendanceRecordEntity recordEntity = saveDTO.getId() == null
                ? attendanceRecordManager.getByStudentAndDate(saveDTO.getStudentId(), saveDTO.getAttendanceDate())
                : attendanceRecordManager.getById(saveDTO.getId());
        if (recordEntity == null) {
            recordEntity = new AttendanceRecordEntity();
            recordEntity.setIsDeleted(0);
            recordEntity.setCreateTime(LocalDateTime.now());
        }
        recordEntity.setStudentId(saveDTO.getStudentId());
        recordEntity.setAttendanceDate(saveDTO.getAttendanceDate());
        recordEntity.setSignInTime(saveDTO.getSignInTime());
        recordEntity.setSignOutTime(saveDTO.getSignOutTime());
        recordEntity.setStatus(saveDTO.getStatus());
        recordEntity.setRemark(saveDTO.getRemark());
        recordEntity.setUpdateTime(LocalDateTime.now());
        attendanceRecordManager.save(recordEntity);
        return toVO(recordEntity, studentEntity);
    }

    /**
     * 查询出勤列表。
     *
     * @param queryDTO 查询对象
     * @return 出勤列表
     */
    @Override
    public List<AttendanceRecordVO> list(AttendanceRecordQueryDTO queryDTO) {
        AuthAssert.requireAdmin();
        Map<Long, StudentEntity> studentMap = studentManager.listAll().stream().collect(Collectors.toMap(StudentEntity::getId, Function.identity()));
        return attendanceRecordManager.listAll().stream()
                .filter(record -> queryDTO.getStudentId() == null || queryDTO.getStudentId().equals(record.getStudentId()))
                .filter(record -> queryDTO.getStartDate() == null || !record.getAttendanceDate().isBefore(queryDTO.getStartDate()))
                .filter(record -> queryDTO.getEndDate() == null || !record.getAttendanceDate().isAfter(queryDTO.getEndDate()))
                .sorted(Comparator.comparing(AttendanceRecordEntity::getAttendanceDate).reversed())
                .map(record -> toVO(record, studentMap.get(record.getStudentId())))
                .toList();
    }

    /**
     * 查询我的历史记录。
     *
     * @return 记录列表
     */
    @Override
    public List<AttendanceRecordVO> myHistory() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        if (currentUser.getStudentId() == null) {
            throw new BizException(400, "当前账号不是学生账号");
        }
        StudentEntity studentEntity = requireStudent(currentUser.getStudentId());
        return attendanceRecordManager.listAll().stream()
                .filter(record -> currentUser.getStudentId().equals(record.getStudentId()))
                .sorted(Comparator.comparing(AttendanceRecordEntity::getAttendanceDate).reversed())
                .map(record -> toVO(record, studentEntity))
                .toList();
    }

    /**
     * 校验并获取学生。
     *
     * @param studentId 学生主键
     * @return 学生实体
     */
    private StudentEntity requireStudent(Long studentId) {
        StudentEntity studentEntity = studentManager.getById(studentId);
        if (studentEntity == null) {
            throw new BizException(404, "学生不存在");
        }
        return studentEntity;
    }

    /**
     * 转换出勤视图对象。
     *
     * @param entity 出勤实体
     * @param studentEntity 学生实体
     * @return 视图对象
     */
    private AttendanceRecordVO toVO(AttendanceRecordEntity entity, StudentEntity studentEntity) {
        AttendanceRecordVO vo = new AttendanceRecordVO();
        vo.setId(entity.getId());
        vo.setStudentId(entity.getStudentId());
        vo.setStudentName(studentEntity == null ? "未知学生" : studentEntity.getName());
        vo.setAttendanceDate(entity.getAttendanceDate());
        vo.setSignInTime(entity.getSignInTime());
        vo.setSignOutTime(entity.getSignOutTime());
        vo.setStatus(entity.getStatus());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}
