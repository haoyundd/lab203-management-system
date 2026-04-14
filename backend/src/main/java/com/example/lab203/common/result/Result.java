package com.example.lab203.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ?????????
 *
 * @param <T> ????
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    /**
     * ???????
     *
     * @param data ????
     * @return ????
     * @param <T> ????
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    /**
     * ???????
     *
     * @return ????
     * @param <T> ????
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    /**
     * ???????
     *
     * @param code ???
     * @param message ????
     * @return ????
     * @param <T> ????
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
