package com.example.lab203.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * ?????????
 */
@Data
public class CleaningScheduleQueryDTO {

    private LocalDate scheduleDate;
    private Long studentId;
}
