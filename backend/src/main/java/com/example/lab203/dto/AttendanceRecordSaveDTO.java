package com.example.lab203.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ????????????
 */
@Data
public class AttendanceRecordSaveDTO {

    private Long id;

    @NotNull(message = "??????")
    private Long studentId;

    @NotNull(message = "????????")
    private LocalDate attendanceDate;

    private LocalDateTime signInTime;
    private LocalDateTime signOutTime;

    @NotBlank(message = "??????")
    private String status;

    private String remark;
}
