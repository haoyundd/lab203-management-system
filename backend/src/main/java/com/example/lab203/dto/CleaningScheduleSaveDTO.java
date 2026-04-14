package com.example.lab203.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * ?????????
 */
@Data
public class CleaningScheduleSaveDTO {

    private Long id;

    @NotNull(message = "????????")
    private LocalDate scheduleDate;

    @NotNull(message = "????????")
    private Long studentId;

    @NotBlank(message = "????????")
    private String taskContent;

    private String remark;
}
