package com.example.lab203.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ?????
 */
@Data
public class StudentEntity {

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
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
