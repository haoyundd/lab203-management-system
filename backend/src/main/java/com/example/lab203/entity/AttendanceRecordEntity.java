package com.example.lab203.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ???????
 */
@Data
public class AttendanceRecordEntity {

    private Long id;
    private Long studentId;
    private LocalDate attendanceDate;
    private LocalDateTime signInTime;
    private LocalDateTime signOutTime;
    private String status;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
