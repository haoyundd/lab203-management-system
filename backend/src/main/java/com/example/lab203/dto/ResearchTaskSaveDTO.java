package com.example.lab203.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ?????????
 */
@Data
public class ResearchTaskSaveDTO {

    @NotBlank(message = "????????")
    private String taskContent;
}
