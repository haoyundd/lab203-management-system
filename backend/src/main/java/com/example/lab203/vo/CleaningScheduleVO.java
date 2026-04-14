package com.example.lab203.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ?????????
 */
@Data
public class CleaningScheduleVO {

    private Long id;
    private LocalDate scheduleDate;
    private Long studentId;
    private String studentName;
    private String taskContent;
    private String status;
    private LocalDateTime finishedTime;
    private String remark;
}
