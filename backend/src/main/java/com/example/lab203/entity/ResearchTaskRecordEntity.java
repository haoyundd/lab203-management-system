package com.example.lab203.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ?????????
 */
@Data
public class ResearchTaskRecordEntity {

    private Long id;
    private Long studentId;
    private LocalDate taskDate;
    private String taskContent;
    private LocalDateTime lastModifiedTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
