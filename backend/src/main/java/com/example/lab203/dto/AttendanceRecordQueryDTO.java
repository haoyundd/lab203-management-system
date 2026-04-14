package com.example.lab203.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * ?????????
 */
@Data
public class AttendanceRecordQueryDTO {

    private Long studentId;
    private LocalDate startDate;
    private LocalDate endDate;
}
