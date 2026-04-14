package com.example.lab203.service;

import com.example.lab203.dto.DeskItemSaveDTO;
import com.example.lab203.vo.DeskItemVO;

import java.util.List;

/**
 * ?????????
 */
public interface DeskItemService {

    /**
     * ???????
     *
     * @param saveDTO ????
     * @return ????
     */
    DeskItemVO save(DeskItemSaveDTO saveDTO);

    /**
     * ???????
     *
     * @param id ??
     */
    void delete(Long id);

    /**
     * ???????
     *
     * @return ????
     */
    List<DeskItemVO> listAll();

    /**
     * ?????????
     *
     * @return ????
     */
    List<DeskItemVO> myItems();
}
