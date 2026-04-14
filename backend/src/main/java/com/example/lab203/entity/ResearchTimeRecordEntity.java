package com.example.lab203.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ?????????
 */
@Data
public class ResearchTimeRecordEntity {

    private Long id;
    private Long studentId;
    private LocalDate recordDate;
    private BigDecimal hours;
    private String sourceType;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
