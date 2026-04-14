package com.example.lab203.manager;

import com.example.lab203.entity.ResearchTaskRecordEntity;
import com.example.lab203.mapper.ResearchTaskRecordMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * ????????
 */
@Component
public class ResearchTaskRecordManager {

    private final ResearchTaskRecordMapper researchTaskRecordMapper;

    public ResearchTaskRecordManager(ResearchTaskRecordMapper researchTaskRecordMapper) {
        this.researchTaskRecordMapper = researchTaskRecordMapper;
    }

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    public ResearchTaskRecordEntity save(ResearchTaskRecordEntity entity) {
        if (entity.getId() == null) {
            researchTaskRecordMapper.insert(entity);
        } else {
            researchTaskRecordMapper.updateById(entity);
        }
        return entity;
    }

    /**
     * ???????????
     *
     * @param studentId ????
     * @param date ??
     * @return ????
     */
    public ResearchTaskRecordEntity getByStudentAndDate(Long studentId, LocalDate date) {
        return researchTaskRecordMapper.selectByStudentAndDate(studentId, date);
    }

    /**
     * ???????
     *
     * @return ????
     */
    public List<ResearchTaskRecordEntity> listAll() {
        return researchTaskRecordMapper.selectAllActive();
    }
}
