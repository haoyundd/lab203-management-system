package com.example.lab203.service;

import com.example.lab203.common.exception.BizException;
import com.example.lab203.common.util.CurrentUserHolder;
import com.example.lab203.entity.ResearchTimeRecordEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.ResearchTimeRecordManager;
import com.example.lab203.manager.StudentManager;
import com.example.lab203.service.serviceimpl.ResearchTimeRecordServiceImpl;
import com.example.lab203.vo.CurrentUserVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 科研时长服务测试。
 */
@ExtendWith(MockitoExtension.class)
public class ResearchTimeRecordServiceTest {

    @Mock
    private ResearchTimeRecordManager researchTimeRecordManager;

    @Mock
    private StudentManager studentManager;

    @InjectMocks
    private ResearchTimeRecordServiceImpl researchTimeRecordService;

    /**
     * 清理登录上下文。
     */
    @AfterEach
    public void tearDown() {
        CurrentUserHolder.clear();
    }

    /**
     * 验证每天只能生成一次科研时长。
     */
    @Test
    public void shouldRejectGenerateWhenTodayRecordExists() {
        mockCurrentStudent();
        when(researchTimeRecordManager.getByStudentAndDate(eq(1L), eq(LocalDate.now()))).thenReturn(new ResearchTimeRecordEntity());

        BizException exception = Assertions.assertThrows(BizException.class, () -> researchTimeRecordService.generateToday());

        Assertions.assertEquals("今天已经生成过科研时长了", exception.getMessage());
        verify(researchTimeRecordManager, never()).save(any());
    }

    /**
     * 验证首次生成会写入历史并更新学生统计。
     */
    @Test
    public void shouldGenerateTodayResearchTimeAndUpdateStudent() {
        mockCurrentStudent();
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(1L);
        studentEntity.setName("测试学生");
        studentEntity.setLatestResearchHours(BigDecimal.ZERO.setScale(1));
        studentEntity.setTotalResearchHours(BigDecimal.valueOf(8.0).setScale(1));
        when(researchTimeRecordManager.getByStudentAndDate(eq(1L), eq(LocalDate.now()))).thenReturn(null);
        when(studentManager.getById(1L)).thenReturn(studentEntity);

        researchTimeRecordService.generateToday();

        ArgumentCaptor<ResearchTimeRecordEntity> recordCaptor = ArgumentCaptor.forClass(ResearchTimeRecordEntity.class);
        verify(researchTimeRecordManager).save(recordCaptor.capture());
        verify(studentManager).save(studentEntity);
        Assertions.assertEquals(LocalDate.now(), recordCaptor.getValue().getRecordDate());
        Assertions.assertTrue(studentEntity.getTotalResearchHours().compareTo(BigDecimal.valueOf(8.0).setScale(1)) > 0);
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
