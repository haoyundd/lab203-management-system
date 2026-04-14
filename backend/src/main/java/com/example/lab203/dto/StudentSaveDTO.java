package com.example.lab203.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ?????????
 */
@Data
public class StudentSaveDTO {

    private Long id;

    @NotBlank(message = "??????")
    private String studentNo;

    @NotBlank(message = "??????")
    private String name;

    @NotBlank(message = "??????")
    private String grade;

    @NotBlank(message = "??????")
    private String major;

    @NotBlank(message = "????????")
    private String researchDirection;

    @NotBlank(message = "???????")
    private String phone;

    @NotBlank(message = "??????")
    private String email;

    private String avatarPath;
}
