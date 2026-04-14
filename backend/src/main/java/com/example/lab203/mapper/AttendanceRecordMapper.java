package com.example.lab203.mapper;

import com.example.lab203.entity.AttendanceRecordEntity;
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
public interface AttendanceRecordMapper {

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    @Insert("""
            INSERT INTO lab_attendance_record(student_id, attendance_date, sign_in_time, sign_out_time, status, remark, is_deleted, create_time, update_time)
            VALUES(#{studentId}, #{attendanceDate}, #{signInTime}, #{signOutTime}, #{status}, #{remark}, #{isDeleted}, #{createTime}, #{updateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AttendanceRecordEntity entity);

    /**
     * ???????
     *
     * @param entity ????
     * @return ????
     */
    @Update("""
            UPDATE lab_attendance_record
            SET student_id = #{studentId}, attendance_date = #{attendanceDate}, sign_in_time = #{signInTime}, sign_out_time = #{signOutTime},
                status = #{status}, remark = #{remark}, update_time = #{updateTime}
            WHERE id = #{id} AND is_deleted = 0
            """)
    int updateById(AttendanceRecordEntity entity);

    /**
     * ????????
     *
     * @param id ??
     * @return ????
     */
    @Select("SELECT * FROM lab_attendance_record WHERE id = #{id} AND is_deleted = 0 LIMIT 1")
    AttendanceRecordEntity selectById(Long id);

    /**
     * ???????????
     *
     * @param studentId ????
     * @param attendanceDate ??
     * @return ????
     */
    @Select("SELECT * FROM lab_attendance_record WHERE student_id = #{studentId} AND attendance_date = #{attendanceDate} AND is_deleted = 0 LIMIT 1")
    AttendanceRecordEntity selectByStudentAndDate(@Param("studentId") Long studentId, @Param("attendanceDate") LocalDate attendanceDate);

    /**
     * ?????????
     *
     * @return ????
     */
    @Select("SELECT * FROM lab_attendance_record WHERE is_deleted = 0 ORDER BY attendance_date DESC, id DESC")
    List<AttendanceRecordEntity> selectAllActive();
}
