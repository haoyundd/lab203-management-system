package com.example.lab203.service;

import com.example.lab203.dto.ResearchTaskSaveDTO;
import com.example.lab203.vo.ResearchTaskVO;

import java.util.List;

/**
 * ?????????
 */
public interface ResearchTaskRecordService {

    /**
     * ?????????
     *
     * @return ????
     */
    ResearchTaskVO myToday();

    /**
     * ?????????
     *
     * @param saveDTO ????
     * @return ????
     */
    ResearchTaskVO saveToday(ResearchTaskSaveDTO saveDTO);

    /**
     * ?????????
     *
     * @return ????
     */
    List<ResearchTaskVO> myHistory();

    /**
     * ???????????
     *
     * @return ????
     */
    List<ResearchTaskVO> todayList();
}
