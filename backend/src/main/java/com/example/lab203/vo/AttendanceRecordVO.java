package com.example.lab203.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ?????????
 */
@Data
public class AttendanceRecordVO {

    private Long id;
    private Long studentId;
    private String studentName;
    private LocalDate attendanceDate;
    private LocalDateTime signInTime;
    private LocalDateTime signOutTime;
    private String status;
    private String remark;
}
