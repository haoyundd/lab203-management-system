package com.example.lab203.service;

import com.example.lab203.dto.CleaningScheduleQueryDTO;
import com.example.lab203.dto.CleaningScheduleSaveDTO;
import com.example.lab203.vo.CleaningScheduleVO;

import java.util.List;

/**
 * ?????????
 */
public interface CleaningScheduleService {

    /**
     * ?????
     *
     * @param saveDTO ????
     * @return ????
     */
    CleaningScheduleVO save(CleaningScheduleSaveDTO saveDTO);

    /**
     * ?????
     *
     * @param id ??
     * @return ????
     */
    CleaningScheduleVO finish(Long id);

    /**
     * ???????
     *
     * @param queryDTO ????
     * @return ????
     */
    List<CleaningScheduleVO> list(CleaningScheduleQueryDTO queryDTO);

    /**
     * ?????????
     *
     * @return ????
     */
    CleaningScheduleVO myToday();
}
