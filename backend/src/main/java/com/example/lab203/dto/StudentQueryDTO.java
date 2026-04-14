package com.example.lab203.dto;

import lombok.Data;

/**
 * ?????????
 */
@Data
public class StudentQueryDTO {

    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String keyword;
}
