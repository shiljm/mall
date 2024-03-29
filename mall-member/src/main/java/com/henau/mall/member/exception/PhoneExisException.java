package com.henau.mall.member.exception;

public class PhoneExisException extends RuntimeException {

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public PhoneExisException() {
        super("手机号存在");
    }
}
