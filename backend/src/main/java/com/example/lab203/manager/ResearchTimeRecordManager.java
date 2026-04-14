package com.example.lab203.manager;

import com.example.lab203.entity.ResearchTimeRecordEntity;
import com.example.lab203.mapper.ResearchTimeRecordMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * ????????
 */
@Component
public class ResearchTimeRecordManager {

    private final ResearchTimeRecordMapper researchTimeRecordMapper;

    public ResearchTimeRecordManager(ResearchTimeRecordMapper researchTimeRecordMapper) {
        this.researchTimeRecordMapper = researchTimeRecordMapper;
    }

    /**
     * ?????????
     *
     * @param entity ????
     * @return ????
     */
    public ResearchTimeRecordEntity save(ResearchTimeRecordEntity entity) {
        researchTimeRecordMapper.insert(entity);
        return entity;
    }

    /**
     * ???????????
     *
     * @param studentId ????
     * @param date ??
     * @return ????
     */
    public ResearchTimeRecordEntity getByStudentAndDate(Long studentId, LocalDate date) {
        return researchTimeRecordMapper.selectByStudentAndDate(studentId, date);
    }

    /**
     * ???????????
     *
     * @return ????
     */
    public List<ResearchTimeRecordEntity> listAll() {
        return researchTimeRecordMapper.selectAllActive();
    }
}
