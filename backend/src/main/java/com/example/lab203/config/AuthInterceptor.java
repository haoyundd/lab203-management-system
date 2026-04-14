package com.example.lab203.config;

import com.example.lab203.common.annotation.IgnoreAuth;
import com.example.lab203.common.constant.AuthConstants;
import com.example.lab203.common.util.CurrentUserHolder;
import com.example.lab203.service.AuthService;
import com.example.lab203.vo.CurrentUserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ????????
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    /**
     * ????????? token?
     *
     * @param request ????
     * @param response ????
     * @param handler ???
     * @return ????
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        if (handlerMethod.hasMethodAnnotation(IgnoreAuth.class) || handlerMethod.getBeanType().isAnnotationPresent(IgnoreAuth.class)) {
            return true;
        }
        String authorization = request.getHeader(AuthConstants.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(AuthConstants.BEARER_PREFIX)) {
            return true;
        }
        String token = authorization.substring(AuthConstants.BEARER_PREFIX.length());
        CurrentUserVO currentUser = authService.parseCurrentUser(token);
        if (currentUser != null) {
            CurrentUserHolder.set(currentUser);
        }
        return true;
    }

    /**
     * ????????????
     *
     * @param request ????
     * @param response ????
     * @param handler ???
     * @param ex ????
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        CurrentUserHolder.clear();
    }
}
