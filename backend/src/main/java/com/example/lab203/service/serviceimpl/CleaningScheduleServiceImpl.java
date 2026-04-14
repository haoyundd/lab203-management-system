package com.example.lab203.service.serviceimpl;

import com.example.lab203.common.enums.CleaningStatusEnum;
import com.example.lab203.common.exception.BizException;
import com.example.lab203.common.util.AuthAssert;
import com.example.lab203.dto.CleaningScheduleQueryDTO;
import com.example.lab203.dto.CleaningScheduleSaveDTO;
import com.example.lab203.entity.CleaningScheduleEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.CleaningScheduleManager;
import com.example.lab203.manager.StudentManager;
import com.example.lab203.service.CleaningScheduleService;
import com.example.lab203.vo.CleaningScheduleVO;
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
 * 卫生排班服务实现。
 */
@Service
public class CleaningScheduleServiceImpl implements CleaningScheduleService {

    private final CleaningScheduleManager cleaningScheduleManager;
    private final StudentManager studentManager;

    public CleaningScheduleServiceImpl(CleaningScheduleManager cleaningScheduleManager, StudentManager studentManager) {
        this.cleaningScheduleManager = cleaningScheduleManager;
        this.studentManager = studentManager;
    }

    /**
     * 保存排班。
     *
     * @param saveDTO 保存对象
     * @return 排班详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CleaningScheduleVO save(CleaningScheduleSaveDTO saveDTO) {
        AuthAssert.requireAdmin();
        StudentEntity studentEntity = requireStudent(saveDTO.getStudentId());
        CleaningScheduleEntity entity = saveDTO.getId() == null ? new CleaningScheduleEntity() : requireSchedule(saveDTO.getId());
        entity.setScheduleDate(saveDTO.getScheduleDate());
        entity.setStudentId(saveDTO.getStudentId());
        entity.setTaskContent(saveDTO.getTaskContent());
        entity.setRemark(saveDTO.getRemark());
        if (entity.getId() == null) {
            entity.setStatus(CleaningStatusEnum.PENDING.name());
            entity.setIsDeleted(0);
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setUpdateTime(LocalDateTime.now());
        cleaningScheduleManager.save(entity);
        return toVO(entity, studentEntity);
    }

    /**
     * 标记排班完成。
     *
     * @param id 主键
     * @return 排班详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CleaningScheduleVO finish(Long id) {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        CleaningScheduleEntity entity = requireSchedule(id);
        if (!"ADMIN".equals(currentUser.getRole()) && !entity.getStudentId().equals(currentUser.getStudentId())) {
            throw new BizException(403, "只能完成自己的值日任务");
        }
        entity.setStatus(CleaningStatusEnum.DONE.name());
        entity.setFinishedTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        cleaningScheduleManager.save(entity);
        return toVO(entity, requireStudent(entity.getStudentId()));
    }

    /**
     * 查询排班列表。
     *
     * @param queryDTO 查询对象
     * @return 排班列表
     */
    @Override
    public List<CleaningScheduleVO> list(CleaningScheduleQueryDTO queryDTO) {
        AuthAssert.requireAdmin();
        Map<Long, StudentEntity> studentMap = studentManager.listAll().stream().collect(Collectors.toMap(StudentEntity::getId, Function.identity()));
        return cleaningScheduleManager.listAll().stream()
                .filter(schedule -> queryDTO.getScheduleDate() == null || queryDTO.getScheduleDate().equals(schedule.getScheduleDate()))
                .filter(schedule -> queryDTO.getStudentId() == null || queryDTO.getStudentId().equals(schedule.getStudentId()))
                .sorted(Comparator.comparing(CleaningScheduleEntity::getScheduleDate).reversed())
                .map(schedule -> toVO(schedule, studentMap.get(schedule.getStudentId())))
                .toList();
    }

    /**
     * 查询我的今日排班。
     *
     * @return 今日排班
     */
    @Override
    public CleaningScheduleVO myToday() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        if (currentUser.getStudentId() == null) {
            throw new BizException(400, "当前账号不是学生账号");
        }
        StudentEntity studentEntity = requireStudent(currentUser.getStudentId());
        return cleaningScheduleManager.listAll().stream()
                .filter(schedule -> currentUser.getStudentId().equals(schedule.getStudentId()))
                .filter(schedule -> LocalDate.now().equals(schedule.getScheduleDate()))
                .findFirst()
                .map(schedule -> toVO(schedule, studentEntity))
                .orElse(null);
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
     * 校验并获取排班。
     *
     * @param id 主键
     * @return 排班实体
     */
    private CleaningScheduleEntity requireSchedule(Long id) {
        CleaningScheduleEntity entity = cleaningScheduleManager.getById(id);
        if (entity == null) {
            throw new BizException(404, "值日排班不存在");
        }
        return entity;
    }

    /**
     * 转换排班视图对象。
     *
     * @param entity 排班实体
     * @param studentEntity 学生实体
     * @return 视图对象
     */
    private CleaningScheduleVO toVO(CleaningScheduleEntity entity, StudentEntity studentEntity) {
        CleaningScheduleVO vo = new CleaningScheduleVO();
        vo.setId(entity.getId());
        vo.setScheduleDate(entity.getScheduleDate());
        vo.setStudentId(entity.getStudentId());
        vo.setStudentName(studentEntity == null ? "未知学生" : studentEntity.getName());
        vo.setTaskContent(entity.getTaskContent());
        vo.setStatus(entity.getStatus());
        vo.setFinishedTime(entity.getFinishedTime());
        vo.setRemark(entity.getRemark());
        return vo;
    }
}
