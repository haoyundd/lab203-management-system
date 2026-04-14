package com.example.lab203.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ???????
 */
@Data
public class CleaningScheduleEntity {

    private Long id;
    private LocalDate scheduleDate;
    private Long studentId;
    private String taskContent;
    private String status;
    private LocalDateTime finishedTime;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
