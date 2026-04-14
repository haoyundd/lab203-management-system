package com.example.lab203.mapper;

import com.example.lab203.entity.ResearchTimeRecordEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * ???? Mapper?
 */
@Mapper
public interface ResearchTimeRecordMapper {

    /**
     * ?????????
     *
     * @param entity ????
     * @return ????
     */
    @Insert("""
            INSERT INTO lab_research_time_record(student_id, record_date, hours, source_type, is_deleted, create_time, update_time)
            VALUES(#{studentId}, #{recordDate}, #{hours}, #{sourceType}, #{isDeleted}, #{createTime}, #{updateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ResearchTimeRecordEntity entity);

    /**
     * ?????????????
     *
     * @param studentId ????
     * @param recordDate ??
     * @return ????
     */
    @Select("SELECT * FROM lab_research_time_record WHERE student_id = #{studentId} AND record_date = #{recordDate} AND is_deleted = 0 LIMIT 1")
    ResearchTimeRecordEntity selectByStudentAndDate(@Param("studentId") Long studentId, @Param("recordDate") LocalDate recordDate);

    /**
     * ?????????????
     *
     * @return ????
     */
    @Select("SELECT * FROM lab_research_time_record WHERE is_deleted = 0 ORDER BY record_date DESC, id DESC")
    List<ResearchTimeRecordEntity> selectAllActive();
}
