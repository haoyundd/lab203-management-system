package com.example.lab203.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ?????????
 */
@Data
public class DeskItemSaveDTO {

    private Long id;

    @NotNull(message = "??????")
    private Long studentId;

    @NotBlank(message = "????????")
    private String itemName;

    @NotNull(message = "??????")
    @Min(value = 1, message = "????? 1")
    private Integer quantity;

    @NotBlank(message = "????????")
    private String itemStatus;

    private String remark;
}
