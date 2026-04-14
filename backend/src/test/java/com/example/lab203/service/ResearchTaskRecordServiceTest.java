package com.example.lab203.service;

import com.example.lab203.common.exception.BizException;
import com.example.lab203.common.util.CurrentUserHolder;
import com.example.lab203.dto.ResearchTaskSaveDTO;
import com.example.lab203.entity.ResearchTaskRecordEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.ResearchTaskRecordManager;
import com.example.lab203.manager.StudentManager;
import com.example.lab203.service.serviceimpl.ResearchTaskRecordServiceImpl;
import com.example.lab203.vo.CurrentUserVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 科研任务服务测试。
 */
@ExtendWith(MockitoExtension.class)
public class ResearchTaskRecordServiceTest {

    @Mock
    private ResearchTaskRecordManager researchTaskRecordManager;

    @Mock
    private StudentManager studentManager;

    @InjectMocks
    private ResearchTaskRecordServiceImpl researchTaskRecordService;

    /**
     * 清理登录上下文。
     */
    @AfterEach
    public void tearDown() {
        CurrentUserHolder.clear();
    }

    /**
     * 验证学生可以新增当天科研任务。
     */
    @Test
    public void shouldCreateTodayTaskForStudent() {
        mockCurrentStudent();
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(1L);
        studentEntity.setName("测试学生");
        when(studentManager.getById(1L)).thenReturn(studentEntity);
        when(researchTaskRecordManager.getByStudentAndDate(eq(1L), eq(LocalDate.now()))).thenReturn(null);
        ResearchTaskSaveDTO saveDTO = new ResearchTaskSaveDTO();
        saveDTO.setTaskContent("完成实验复现并更新日志");

        researchTaskRecordService.saveToday(saveDTO);

        ArgumentCaptor<ResearchTaskRecordEntity> captor = ArgumentCaptor.forClass(ResearchTaskRecordEntity.class);
        verify(researchTaskRecordManager).save(captor.capture());
        Assertions.assertEquals(LocalDate.now(), captor.getValue().getTaskDate());
        Assertions.assertEquals("完成实验复现并更新日志", captor.getValue().getTaskContent());
    }

    /**
     * 验证非学生账号不能写科研任务。
     */
    @Test
    public void shouldRejectSaveWhenCurrentUserIsNotStudent() {
        CurrentUserVO currentUserVO = new CurrentUserVO();
        currentUserVO.setAccountId(99L);
        currentUserVO.setRole("ADMIN");
        CurrentUserHolder.set(currentUserVO);
        ResearchTaskSaveDTO saveDTO = new ResearchTaskSaveDTO();
        saveDTO.setTaskContent("查看全体任务");

        BizException exception = Assertions.assertThrows(BizException.class, () -> researchTaskRecordService.saveToday(saveDTO));

        Assertions.assertEquals("当前账号不是学生账号", exception.getMessage());
    }

    /**
     * 构造当前学生登录态。
     */
    private void mockCurrentStudent() {
        CurrentUserVO currentUserVO = new CurrentUserVO();
        currentUserVO.setAccountId(10L);
        currentUserVO.setStudentId(1L);
        currentUserVO.setRole("STUDENT");
        CurrentUserHolder.set(currentUserVO);
    }
}
