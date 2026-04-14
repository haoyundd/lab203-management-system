package com.example.lab203.service.serviceimpl;

import com.example.lab203.common.exception.BizException;
import com.example.lab203.common.util.AuthAssert;
import com.example.lab203.dto.ResearchTaskSaveDTO;
import com.example.lab203.entity.ResearchTaskRecordEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.ResearchTaskRecordManager;
import com.example.lab203.manager.StudentManager;
import com.example.lab203.service.ResearchTaskRecordService;
import com.example.lab203.vo.CurrentUserVO;
import com.example.lab203.vo.ResearchTaskVO;
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
 * 科研任务服务实现。
 */
@Service
public class ResearchTaskRecordServiceImpl implements ResearchTaskRecordService {

    private final ResearchTaskRecordManager researchTaskRecordManager;
    private final StudentManager studentManager;

    public ResearchTaskRecordServiceImpl(ResearchTaskRecordManager researchTaskRecordManager, StudentManager studentManager) {
        this.researchTaskRecordManager = researchTaskRecordManager;
        this.studentManager = studentManager;
    }

    /**
     * 查询我的今日任务。
     *
     * @return 今日任务
     */
    @Override
    public ResearchTaskVO myToday() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        if (currentUser.getStudentId() == null) {
            throw new BizException(400, "当前账号不是学生账号");
        }
        StudentEntity studentEntity = requireStudent(currentUser.getStudentId());
        ResearchTaskRecordEntity entity = researchTaskRecordManager.getByStudentAndDate(currentUser.getStudentId(), LocalDate.now());
        return entity == null ? null : toVO(entity, studentEntity);
    }

    /**
     * 保存我今天的科研任务。
     *
     * @param saveDTO 保存对象
     * @return 任务详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResearchTaskVO saveToday(ResearchTaskSaveDTO saveDTO) {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        if (currentUser.getStudentId() == null) {
            throw new BizException(400, "当前账号不是学生账号");
        }
        StudentEntity studentEntity = requireStudent(currentUser.getStudentId());
        ResearchTaskRecordEntity entity = researchTaskRecordManager.getByStudentAndDate(currentUser.getStudentId(), LocalDate.now());
        if (entity == null) {
            entity = new ResearchTaskRecordEntity();
            entity.setStudentId(currentUser.getStudentId());
            entity.setTaskDate(LocalDate.now());
            entity.setIsDeleted(0);
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setTaskContent(saveDTO.getTaskContent());
        entity.setLastModifiedTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        researchTaskRecordManager.save(entity);
        return toVO(entity, studentEntity);
    }

    /**
     * 查询我的任务历史。
     *
     * @return 历史列表
     */
    @Override
    public List<ResearchTaskVO> myHistory() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        if (currentUser.getStudentId() == null) {
            throw new BizException(400, "当前账号不是学生账号");
        }
        StudentEntity studentEntity = requireStudent(currentUser.getStudentId());
        return researchTaskRecordManager.listAll().stream()
                .filter(record -> currentUser.getStudentId().equals(record.getStudentId()))
                .sorted(Comparator.comparing(ResearchTaskRecordEntity::getTaskDate).reversed())
                .map(record -> toVO(record, studentEntity))
                .toList();
    }

    /**
     * 查询全体学生今日任务。
     *
     * @return 今日任务列表
     */
    @Override
    public List<ResearchTaskVO> todayList() {
        AuthAssert.requireAdmin();
        Map<Long, StudentEntity> studentMap = studentManager.listAll().stream().collect(Collectors.toMap(StudentEntity::getId, Function.identity()));
        return researchTaskRecordManager.listAll().stream()
                .filter(record -> LocalDate.now().equals(record.getTaskDate()))
                .sorted(Comparator.comparing(ResearchTaskRecordEntity::getLastModifiedTime).reversed())
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
        return studentEntity;
    }

    /**
     * 转换任务视图对象。
     *
     * @param entity 任务实体
     * @param studentEntity 学生实体
     * @return 视图对象
     */
    private ResearchTaskVO toVO(ResearchTaskRecordEntity entity, StudentEntity studentEntity) {
        ResearchTaskVO vo = new ResearchTaskVO();
        vo.setId(entity.getId());
        vo.setStudentId(entity.getStudentId());
        vo.setStudentName(studentEntity == null ? "未知学生" : studentEntity.getName());
        vo.setTaskDate(entity.getTaskDate());
        vo.setTaskContent(entity.getTaskContent());
        vo.setLastModifiedTime(entity.getLastModifiedTime());
        return vo;
    }
}
