package com.example.lab203.service.serviceimpl;

import com.example.lab203.common.constant.AuthConstants;
import com.example.lab203.common.exception.BizException;
import com.example.lab203.common.util.AuthAssert;
import com.example.lab203.dto.LoginRequestDTO;
import com.example.lab203.entity.AccountEntity;
import com.example.lab203.entity.StudentEntity;
import com.example.lab203.manager.AccountManager;
import com.example.lab203.manager.StudentManager;
import com.example.lab203.service.AuthService;
import com.example.lab203.vo.CurrentUserVO;
import com.example.lab203.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 认证服务实现。
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AccountManager accountManager;
    private final StudentManager studentManager;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AccountManager accountManager, StudentManager studentManager, PasswordEncoder passwordEncoder) {
        this.accountManager = accountManager;
        this.studentManager = studentManager;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 登录并生成 token。
     *
     * @param requestDTO 登录请求
     * @return 登录结果
     */
    @Override
    public LoginVO login(LoginRequestDTO requestDTO) {
        AccountEntity accountEntity = accountManager.getByUsername(requestDTO.getUsername());
        if (accountEntity == null || !passwordEncoder.matches(requestDTO.getPassword(), accountEntity.getPasswordHash())) {
            throw new BizException(400, "用户名或密码错误");
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime expireTime = LocalDateTime.now().plusDays(AuthConstants.TOKEN_EXPIRE_DAYS);
        accountManager.refreshToken(accountEntity.getId(), token, expireTime);
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setCurrentUser(buildCurrentUser(accountEntity));
        log.info("[AuthService] 登录成功: {}", requestDTO.getUsername());
        return loginVO;
    }

    /**
     * 退出登录。
     */
    @Override
    public void logout() {
        CurrentUserVO currentUser = AuthAssert.requireLogin();
        accountManager.clearToken(currentUser.getAccountId());
    }

    /**
     * 获取当前登录用户。
     *
     * @return 当前用户
     */
    @Override
    public CurrentUserVO currentUser() {
        return AuthAssert.requireLogin();
    }

    /**
     * 根据 token 解析当前用户。
     *
     * @param token token
     * @return 当前用户
     */
    @Override
    public CurrentUserVO parseCurrentUser(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        AccountEntity accountEntity = accountManager.getByToken(token);
        if (accountEntity == null || accountEntity.getTokenExpireTime() == null || accountEntity.getTokenExpireTime().isBefore(LocalDateTime.now())) {
            return null;
        }
        return buildCurrentUser(accountEntity);
    }

    /**
     * 构建当前登录用户对象。
     *
     * @param accountEntity 账号实体
     * @return 当前用户视图对象
     */
    private CurrentUserVO buildCurrentUser(AccountEntity accountEntity) {
        CurrentUserVO currentUserVO = new CurrentUserVO();
        currentUserVO.setAccountId(accountEntity.getId());
        currentUserVO.setStudentId(accountEntity.getStudentId());
        currentUserVO.setUsername(accountEntity.getUsername());
        currentUserVO.setDisplayName(accountEntity.getDisplayName());
        currentUserVO.setRole(accountEntity.getRole());
        if (accountEntity.getStudentId() != null) {
            StudentEntity studentEntity = studentManager.getById(accountEntity.getStudentId());
            if (studentEntity != null) {
                currentUserVO.setAvatarPath(studentEntity.getAvatarPath());
            }
        }
        return currentUserVO;
    }
}
