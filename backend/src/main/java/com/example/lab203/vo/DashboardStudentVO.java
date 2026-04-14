package com.example.lab203.vo;

import lombok.Data;

import java.util.List;

/**
 * ?????????
 */
@Data
public class DashboardStudentVO {

    private StudentVO student;
    private AttendanceRecordVO todayAttendance;
    private CleaningScheduleVO todayCleaning;
    private ResearchTaskVO todayTask;
    private ResearchTimeRecordVO todayResearchTime;
    private List<DeskItemVO> deskItems;
}
