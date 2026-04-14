package com.example.lab203.mapper;

import com.example.lab203.entity.ResearchTaskRecordEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

/**
 * ???? Mapper?
 */
@Mapper
public interface ResearchTaskRecordMapper {

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    @Insert("""
            INSERT INTO lab_research_task_record(student_id, task_date, task_content, last_modified_time, is_deleted, create_time, update_time)
            VALUES(#{studentId}, #{taskDate}, #{taskContent}, #{lastModifiedTime}, #{isDeleted}, #{createTime}, #{updateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ResearchTaskRecordEntity entity);

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    @Update("""
            UPDATE lab_research_task_record
            SET task_content = #{taskContent}, last_modified_time = #{lastModifiedTime}, update_time = #{updateTime}
            WHERE id = #{id} AND is_deleted = 0
            """)
    int updateById(ResearchTaskRecordEntity entity);

    /**
     * ???????????
     *
     * @param studentId ????
     * @param taskDate ??
     * @return ????
     */
    @Select("SELECT * FROM lab_research_task_record WHERE student_id = #{studentId} AND task_date = #{taskDate} AND is_deleted = 0 LIMIT 1")
    ResearchTaskRecordEntity selectByStudentAndDate(@Param("studentId") Long studentId, @Param("taskDate") LocalDate taskDate);

    /**
     * ?????????
     *
     * @return ????
     */
    @Select("SELECT * FROM lab_research_task_record WHERE is_deleted = 0 ORDER BY task_date DESC, id DESC")
    List<ResearchTaskRecordEntity> selectAllActive();
}
