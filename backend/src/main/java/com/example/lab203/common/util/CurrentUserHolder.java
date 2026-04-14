package com.example.lab203.common.util;

import com.example.lab203.vo.CurrentUserVO;

/**
 * ??????????
 */
public final class CurrentUserHolder {

    private static final ThreadLocal<CurrentUserVO> HOLDER = new ThreadLocal<>();

    private CurrentUserHolder() {
    }

    /**
     * ???????
     *
     * @param currentUser ????
     */
    public static void set(CurrentUserVO currentUser) {
        HOLDER.set(currentUser);
    }

    /**
     * ???????
     *
     * @return ????
     */
    public static CurrentUserVO get() {
        return HOLDER.get();
    }

    /**
     * ???????
     */
    public static void clear() {
        HOLDER.remove();
    }
}
