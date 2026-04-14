package com.example.lab203.service;

import com.example.lab203.common.result.PageResult;
import com.example.lab203.dto.StudentQueryDTO;
import com.example.lab203.dto.StudentSaveDTO;
import com.example.lab203.vo.StudentVO;

import java.util.List;

/**
 * ???????
 */
public interface StudentService {

    /**
     * ???????
     *
     * @param queryDTO ????
     * @return ????
     */
    PageResult<StudentVO> page(StudentQueryDTO queryDTO);

    /**
     * ???????
     *
     * @param id ??
     * @return ????
     */
    StudentVO detail(Long id);

    /**
     * ?????
     *
     * @param saveDTO ????
     * @return ????
     */
    StudentVO save(StudentSaveDTO saveDTO);

    /**
     * ?????
     *
     * @param id ??
     */
    void delete(Long id);

    /**
     * ???????
     *
     * @return ????
     */
    List<StudentVO> listAll();
}
