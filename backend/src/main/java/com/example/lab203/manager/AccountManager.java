package com.example.lab203.manager;

import com.example.lab203.entity.AccountEntity;
import com.example.lab203.mapper.AccountMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ????????
 */
@Component
public class AccountManager {

    private final AccountMapper accountMapper;

    public AccountManager(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    /**
     * ?????
     *
     * @param entity ????
     * @return ??????
     */
    public AccountEntity save(AccountEntity entity) {
        accountMapper.insert(entity);
        return entity;
    }

    /**
     * ?????????
     *
     * @param username ???
     * @return ????
     */
    public AccountEntity getByUsername(String username) {
        return accountMapper.selectByUsername(username);
    }

    /**
     * ? token ?????
     *
     * @param token token
     * @return ????
     */
    public AccountEntity getByToken(String token) {
        return accountMapper.selectByToken(token);
    }

    /**
     * ?? token?
     *
     * @param id ??
     * @param token token
     * @param expireTime ????
     */
    public void refreshToken(Long id, String token, LocalDateTime expireTime) {
        accountMapper.updateToken(id, token, expireTime, LocalDateTime.now());
    }

    /**
     * ?? token?
     *
     * @param id ??
     */
    public void clearToken(Long id) {
        accountMapper.clearToken(id, LocalDateTime.now());
    }

    /**
     * ???????
     *
     * @return ????
     */
    public List<AccountEntity> listAll() {
        return accountMapper.selectAllActive();
    }
}
