package com.example.lab203.service;

import com.example.lab203.dto.LoginRequestDTO;
import com.example.lab203.vo.CurrentUserVO;
import com.example.lab203.vo.LoginVO;

/**
 * ???????
 */
public interface AuthService {

    /**
     * ???
     *
     * @param requestDTO ????
     * @return ????
     */
    LoginVO login(LoginRequestDTO requestDTO);

    /**
     * ?????
     */
    void logout();

    /**
     * ?????????
     *
     * @return ????
     */
    CurrentUserVO currentUser();

    /**
     * ?? token?
     *
     * @param token token
     * @return ????
     */
    CurrentUserVO parseCurrentUser(String token);
}
