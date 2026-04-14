package com.example.lab203.common.util;

import com.example.lab203.common.enums.AccountRoleEnum;
import com.example.lab203.common.exception.BizException;
import com.example.lab203.vo.CurrentUserVO;

/**
 * ???????
 */
public final class AuthAssert {

    private AuthAssert() {
    }

    /**
     * ????????
     *
     * @return ????
     */
    public static CurrentUserVO requireLogin() {
        CurrentUserVO currentUser = CurrentUserHolder.get();
        if (currentUser == null) {
            throw new BizException(401, "?????????");
        }
        return currentUser;
    }

    /**
     * ???????????
     *
     * @return ????
     */
    public static CurrentUserVO requireAdmin() {
        CurrentUserVO currentUser = requireLogin();
        if (!AccountRoleEnum.ADMIN.name().equals(currentUser.getRole())) {
            throw new BizException(403, "??????????");
        }
        return currentUser;
    }
}
