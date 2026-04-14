package com.example.lab203.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ???????
 */
@Data
public class StudentVO {

    private Long id;
    private String studentNo;
    private String name;
    private String grade;
    private String major;
    private String researchDirection;
    private String phone;
    private String email;
    private String avatarPath;
    private BigDecimal latestResearchHours;
    private BigDecimal totalResearchHours;
}
