package com.example.lab203.service;

import com.example.lab203.dto.AttendanceRecordQueryDTO;
import com.example.lab203.dto.AttendanceRecordSaveDTO;
import com.example.lab203.vo.AttendanceRecordVO;

import java.util.List;

/**
 * ???????
 */
public interface AttendanceRecordService {

    /**
     * ?????
     *
     * @return ????
     */
    AttendanceRecordVO signIn();

    /**
     * ?????
     *
     * @return ????
     */
    AttendanceRecordVO signOut();

    /**
     * ?????????
     *
     * @param saveDTO ????
     * @return ????
     */
    AttendanceRecordVO saveByAdmin(AttendanceRecordSaveDTO saveDTO);

    /**
     * ???????
     *
     * @param queryDTO ????
     * @return ????
     */
    List<AttendanceRecordVO> list(AttendanceRecordQueryDTO queryDTO);

    /**
     * ?????????
     *
     * @return ????
     */
    List<AttendanceRecordVO> myHistory();
}
