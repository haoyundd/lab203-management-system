package com.example.lab203.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ?????????
 */
@Data
public class ResearchTaskVO {

    private Long id;
    private Long studentId;
    private String studentName;
    private LocalDate taskDate;
    private String taskContent;
    private LocalDateTime lastModifiedTime;
}
