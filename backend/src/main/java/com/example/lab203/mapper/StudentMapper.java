package com.example.lab203.mapper;

import com.example.lab203.entity.StudentEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ?? Mapper?
 */
@Mapper
public interface StudentMapper {

    /**
     * ?????
     *
     * @param entity ????
     * @return ????
     */
    @Insert("""
            INSERT INTO lab_student(student_no, name, grade, major, research_direction, phone, email, avatar_path, latest_research_hours, total_research_hours, is_deleted, create_time, update_time)
            VALUES(#{studentNo}, #{name}, #{grade}, #{major}, #{researchDirection}, #{phone}, #{email}, #{avatarPath}, #{latestResearchHours}, #{totalResearchHours}, #{isDeleted}, #{createTime}, #{updateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(StudentEntity entity);

    /**
     * ?????
     *
     * @param entity ????
     * @return ????
     */
    @Update("""
            UPDATE lab_student
            SET student_no = #{studentNo}, name = #{name}, grade = #{grade}, major = #{major}, research_direction = #{researchDirection},
                phone = #{phone}, email = #{email}, avatar_path = #{avatarPath}, latest_research_hours = #{latestResearchHours},
                total_research_hours = #{totalResearchHours}, update_time = #{updateTime}
            WHERE id = #{id} AND is_deleted = 0
            """)
    int updateById(StudentEntity entity);

    /**
     * ???????
     *
     * @param id ??
     * @param updateTime ????
     * @return ????
     */
    @Update("UPDATE lab_student SET is_deleted = 1, update_time = #{updateTime} WHERE id = #{id} AND is_deleted = 0")
    int logicDelete(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    /**
     * ????????
     *
     * @param id ??
     * @return ????
     */
    @Select("SELECT * FROM lab_student WHERE id = #{id} AND is_deleted = 0 LIMIT 1")
    StudentEntity selectById(Long id);

    /**
     * ????????
     *
     * @param studentNo ??
     * @return ????
     */
    @Select("SELECT * FROM lab_student WHERE student_no = #{studentNo} AND is_deleted = 0 LIMIT 1")
    StudentEntity selectByStudentNo(String studentNo);

    /**
     * ???????
     *
     * @return ????
     */
    @Select("SELECT * FROM lab_student WHERE is_deleted = 0 ORDER BY id ASC")
    List<StudentEntity> selectAllActive();
}
