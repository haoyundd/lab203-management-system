package com.example.lab203.vo;

import lombok.Data;

import java.util.List;

/**
 * ??????????
 */
@Data
public class DashboardAdminVO {

    private Integer studentCount;
    private Integer signedInCount;
    private Integer taskFilledCount;
    private Integer cleaningDoneCount;
    private List<AttendanceRecordVO> todayAttendance;
    private List<ResearchTaskVO> todayTasks;
    private List<CleaningScheduleVO> todayCleaning;
}
