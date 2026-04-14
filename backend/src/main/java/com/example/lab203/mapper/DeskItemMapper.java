package com.example.lab203.mapper;

import com.example.lab203.entity.DeskItemEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ???? Mapper?
 */
@Mapper
public interface DeskItemMapper {

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    @Insert("""
            INSERT INTO lab_desk_item(student_id, item_name, quantity, item_status, remark, is_deleted, create_time, update_time)
            VALUES(#{studentId}, #{itemName}, #{quantity}, #{itemStatus}, #{remark}, #{isDeleted}, #{createTime}, #{updateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DeskItemEntity entity);

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    @Update("""
            UPDATE lab_desk_item
            SET student_id = #{studentId}, item_name = #{itemName}, quantity = #{quantity}, item_status = #{itemStatus},
                remark = #{remark}, update_time = #{updateTime}
            WHERE id = #{id} AND is_deleted = 0
            """)
    int updateById(DeskItemEntity entity);

    /**
     * ???????
     *
     * @param id ??
     * @param updateTime ????
     * @return ????
     */
    @Update("UPDATE lab_desk_item SET is_deleted = 1, update_time = #{updateTime} WHERE id = #{id} AND is_deleted = 0")
    int logicDelete(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    /**
     * ?????????
     *
     * @return ????
     */
    @Select("SELECT * FROM lab_desk_item WHERE is_deleted = 0 ORDER BY id DESC")
    List<DeskItemEntity> selectAllActive();

    /**
     * ????????
     *
     * @param id ??
     * @return ????
     */
    @Select("SELECT * FROM lab_desk_item WHERE id = #{id} AND is_deleted = 0 LIMIT 1")
    DeskItemEntity selectById(Long id);
}
