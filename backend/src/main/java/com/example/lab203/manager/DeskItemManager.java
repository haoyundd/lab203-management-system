package com.example.lab203.manager;

import com.example.lab203.entity.DeskItemEntity;
import com.example.lab203.mapper.DeskItemMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ????????
 */
@Component
public class DeskItemManager {

    private final DeskItemMapper deskItemMapper;

    public DeskItemManager(DeskItemMapper deskItemMapper) {
        this.deskItemMapper = deskItemMapper;
    }

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    public DeskItemEntity save(DeskItemEntity entity) {
        if (entity.getId() == null) {
            deskItemMapper.insert(entity);
        } else {
            deskItemMapper.updateById(entity);
        }
        return entity;
    }

    /**
     * ???????
     *
     * @param id ??
     */
    public void delete(Long id) {
        deskItemMapper.logicDelete(id, LocalDateTime.now());
    }

    /**
     * ??????????
     *
     * @param id ??
     * @return ????
     */
    public DeskItemEntity getById(Long id) {
        return deskItemMapper.selectById(id);
    }

    /**
     * ?????????
     *
     * @return ????
     */
    public List<DeskItemEntity> listAll() {
        return deskItemMapper.selectAllActive();
    }
}
