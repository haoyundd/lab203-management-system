package com.example.lab203.manager;

import com.example.lab203.entity.StudentEntity;
import com.example.lab203.mapper.StudentMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ????????
 */
@Component
public class StudentManager {

    private final StudentMapper studentMapper;

    public StudentManager(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    /**
     * ?????
     *
     * @param entity ????
     * @return ??????
     */
    public StudentEntity save(StudentEntity entity) {
        if (entity.getId() == null) {
            studentMapper.insert(entity);
        } else {
            studentMapper.updateById(entity);
        }
        return entity;
    }

    /**
     * ????????
     *
     * @param id ??
     * @return ????
     */
    public StudentEntity getById(Long id) {
        return studentMapper.selectById(id);
    }

    /**
     * ????????
     *
     * @param studentNo ??
     * @return ????
     */
    public StudentEntity getByStudentNo(String studentNo) {
        return studentMapper.selectByStudentNo(studentNo);
    }

    /**
     * ?????????
     *
     * @return ????
     */
    public List<StudentEntity> listAll() {
        return studentMapper.selectAllActive();
    }

    /**
     * ???????
     *
     * @param id ??
     */
    public void delete(Long id) {
        studentMapper.logicDelete(id, LocalDateTime.now());
    }
}
