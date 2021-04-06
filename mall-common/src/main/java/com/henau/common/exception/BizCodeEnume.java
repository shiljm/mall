package com.henau.common.exception;

/**
 * 3.维护错误码后需要维护错误描述，将他们定义为枚举形式
 * 错误码列表
 *  10：通用
 *      001：参数格式校验
 *      002：短信验证码频率太高
 *  11：商品
 *  12：订单
 *  13：购物车
 *  14：物流
 *  15：用户
 */

public enum BizCodeEnume {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数格式检验失败"),
    SMS_CODE_EXCEPTION(10002, "验证码获取频率太高，稍后再试"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架异常"),
    USER_EXIST_EXVEPTION(150001,"用户存在"),
    PHONE_EXIST_EXVEPTION(150002,"手机号存在"),
    LOGINACCT_PASSWORD_INVAILD_EXVEPTION(150003,"账号密码错误");

    private int code;
    private String msg;
    BizCodeEnume(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
