package com.example.lab203.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * ?????????
 */
@Data
public class ResearchTimeRecordVO {

    private Long id;
    private Long studentId;
    private String studentName;
    private LocalDate recordDate;
    private BigDecimal hours;
    private String sourceType;
}
