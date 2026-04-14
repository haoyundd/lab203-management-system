package com.example.lab203.mapper;

import com.example.lab203.entity.CleaningScheduleEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ???? Mapper?
 */
@Mapper
public interface CleaningScheduleMapper {

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    @Insert("""
            INSERT INTO lab_cleaning_schedule(schedule_date, student_id, task_content, status, finished_time, remark, is_deleted, create_time, update_time)
            VALUES(#{scheduleDate}, #{studentId}, #{taskContent}, #{status}, #{finishedTime}, #{remark}, #{isDeleted}, #{createTime}, #{updateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CleaningScheduleEntity entity);

    /**
     * ?????
     *
     * @param entity ????
     * @return ????
     */
    @Update("""
            UPDATE lab_cleaning_schedule
            SET schedule_date = #{scheduleDate}, student_id = #{studentId}, task_content = #{taskContent}, status = #{status},
                finished_time = #{finishedTime}, remark = #{remark}, update_time = #{updateTime}
            WHERE id = #{id} AND is_deleted = 0
            """)
    int updateById(CleaningScheduleEntity entity);

    /**
     * ????????
     *
     * @param id ??
     * @return ????
     */
    @Select("SELECT * FROM lab_cleaning_schedule WHERE id = #{id} AND is_deleted = 0 LIMIT 1")
    CleaningScheduleEntity selectById(Long id);

    /**
     * ?????????
     *
     * @return ????
     */
    @Select("SELECT * FROM lab_cleaning_schedule WHERE is_deleted = 0 ORDER BY schedule_date DESC, id DESC")
    List<CleaningScheduleEntity> selectAllActive();
}
