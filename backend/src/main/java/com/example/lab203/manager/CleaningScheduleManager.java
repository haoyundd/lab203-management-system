package com.example.lab203.manager;

import com.example.lab203.entity.CleaningScheduleEntity;
import com.example.lab203.mapper.CleaningScheduleMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ????????
 */
@Component
public class CleaningScheduleManager {

    private final CleaningScheduleMapper cleaningScheduleMapper;

    public CleaningScheduleManager(CleaningScheduleMapper cleaningScheduleMapper) {
        this.cleaningScheduleMapper = cleaningScheduleMapper;
    }

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    public CleaningScheduleEntity save(CleaningScheduleEntity entity) {
        if (entity.getId() == null) {
            cleaningScheduleMapper.insert(entity);
        } else {
            cleaningScheduleMapper.updateById(entity);
        }
        return entity;
    }

    /**
     * ????????
     *
     * @param id ??
     * @return ????
     */
    public CleaningScheduleEntity getById(Long id) {
        return cleaningScheduleMapper.selectById(id);
    }

    /**
     * ???????
     *
     * @return ????
     */
    public List<CleaningScheduleEntity> listAll() {
        return cleaningScheduleMapper.selectAllActive();
    }
}
