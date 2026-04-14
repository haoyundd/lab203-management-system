package com.example.lab203.controller;

import com.example.lab203.common.annotation.IgnoreAuth;
import com.example.lab203.common.result.Result;
import com.example.lab203.dto.LoginRequestDTO;
import com.example.lab203.service.AuthService;
import com.example.lab203.vo.CurrentUserVO;
import com.example.lab203.vo.LoginVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器。
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 账号密码登录。
     *
     * @param requestDTO 登录对象
     * @return 登录结果
     */
    @IgnoreAuth
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        return Result.success(authService.login(requestDTO));
    }

    /**
     * 退出登录。
     *
     * @return 空结果
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.success();
    }

    /**
     * 获取当前用户。
     *
     * @return 当前用户
     */
    @GetMapping("/current-user")
    public Result<CurrentUserVO> currentUser() {
        return Result.success(authService.currentUser());
    }
}
