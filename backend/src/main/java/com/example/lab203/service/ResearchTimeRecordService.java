package com.example.lab203.service;

import com.example.lab203.vo.ResearchTimeRecordVO;

import java.util.List;

/**
 * ?????????
 */
public interface ResearchTimeRecordService {

    /**
     * ?????????
     *
     * @return ????
     */
    ResearchTimeRecordVO generateToday();

    /**
     * ???????????
     *
     * @return ????
     */
    List<ResearchTimeRecordVO> myHistory();

    /**
     * ???????????
     *
     * @return ????
     */
    List<ResearchTimeRecordVO> listAll();
}
