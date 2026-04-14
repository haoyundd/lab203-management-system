package com.example.lab203.manager;

import com.example.lab203.entity.AttendanceRecordEntity;
import com.example.lab203.mapper.AttendanceRecordMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * ????????
 */
@Component
public class AttendanceRecordManager {

    private final AttendanceRecordMapper attendanceRecordMapper;

    public AttendanceRecordManager(AttendanceRecordMapper attendanceRecordMapper) {
        this.attendanceRecordMapper = attendanceRecordMapper;
    }

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    public AttendanceRecordEntity save(AttendanceRecordEntity entity) {
        if (entity.getId() == null) {
            attendanceRecordMapper.insert(entity);
        } else {
            attendanceRecordMapper.updateById(entity);
        }
        return entity;
    }

    /**
     * ????????
     *
     * @param id ??
     * @return ????
     */
    public AttendanceRecordEntity getById(Long id) {
        return attendanceRecordMapper.selectById(id);
    }

    /**
     * ???????????
     *
     * @param studentId ????
     * @param date ??
     * @return ????
     */
    public AttendanceRecordEntity getByStudentAndDate(Long studentId, LocalDate date) {
        return attendanceRecordMapper.selectByStudentAndDate(studentId, date);
    }

    /**
     * ???????
     *
     * @return ????
     */
    public List<AttendanceRecordEntity> listAll() {
        return attendanceRecordMapper.selectAllActive();
    }
}
