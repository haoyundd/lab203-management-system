package com.example.lab203.mapper;

import com.example.lab203.entity.AccountEntity;
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
public interface AccountMapper {

    /**
     * ?????
     *
     * @param entity ????
     * @return ????
     */
    @Insert("""
            INSERT INTO lab_account(username, password_hash, role, student_id, display_name, token, token_expire_time, is_deleted, create_time, update_time)
            VALUES(#{username}, #{passwordHash}, #{role}, #{studentId}, #{displayName}, #{token}, #{tokenExpireTime}, #{isDeleted}, #{createTime}, #{updateTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AccountEntity entity);

    /**
     * ?????????
     *
     * @param username ???
     * @return ????
     */
    @Select("SELECT * FROM lab_account WHERE username = #{username} AND is_deleted = 0 LIMIT 1")
    AccountEntity selectByUsername(String username);

    /**
     * ? token ?????
     *
     * @param token ?? token
     * @return ????
     */
    @Select("SELECT * FROM lab_account WHERE token = #{token} AND is_deleted = 0 LIMIT 1")
    AccountEntity selectByToken(String token);

    /**
     * ???? token?
     *
     * @param id ??
     * @param token token
     * @param tokenExpireTime ????
     * @param updateTime ????
     * @return ????
     */
    @Update("UPDATE lab_account SET token = #{token}, token_expire_time = #{tokenExpireTime}, update_time = #{updateTime} WHERE id = #{id} AND is_deleted = 0")
    int updateToken(@Param("id") Long id,
                    @Param("token") String token,
                    @Param("tokenExpireTime") LocalDateTime tokenExpireTime,
                    @Param("updateTime") LocalDateTime updateTime);

    /**
     * ???? token?
     *
     * @param id ??
     * @param updateTime ????
     * @return ????
     */
    @Update("UPDATE lab_account SET token = NULL, token_expire_time = NULL, update_time = #{updateTime} WHERE id = #{id} AND is_deleted = 0")
    int clearToken(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    /**
     * ?????????
     *
     * @return ????
     */
    @Select("SELECT * FROM lab_account WHERE is_deleted = 0 ORDER BY id ASC")
    List<AccountEntity> selectAllActive();
}
