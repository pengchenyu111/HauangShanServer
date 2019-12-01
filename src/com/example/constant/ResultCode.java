package com.example.constant;

/**
 * 这个文档是用来解释返回码和返回信息的
 *
 * 返回码和返回信息是为了方便查看Android客户端向服务器发请求后，服务器返回的  处理异常或成功标识信息，相当于404、500等，不是返回的具体数据信息
 *
 * 返回码统一命名为 : resultCode
 * 返回信息统一命名为 ： resultMessage
 */
public class ResultCode {

    public static final String LOGIN_SUCCESS= "001";
    public static final String LOGIN_SUCCESS_MSG= "登录成功";

    public static final String LOGIN_DATA_WRONG= "002";
    public static final String LOGIN_DATA_WRONG_MSG= "账号或密码错误";

    public static final String LOGIN_DATA_NULL= "003";
    public static final String LOGIN_DATA_NULL_MSG= "账号或密码不能为空";

    public static final String LOGIN_ACCOUNT_ILLEGAL= "004";
    public static final String LOGIN_ACCOUNT_ILLEGAL_MSG= "账号含有非法字符";

    public static final String LOGIN_PASSWORD_ILLEGAL= "005";
    public static final String LOGIN_PASSWORD_ILLEGAL_MSG= "密码含有非法字符";

    public static final String DELETE_ADMIN_SUCCESS= "006";
    public static final String DELETE_ADMIN_SUCCESS_MSG= "该管理员已被成功删除";

    public static final String DELETE_ADMIN_FAIL= "007";
    public static final String DELETE_ADMIN_FAIL_MSG= "该管理员删除失败";

    public static final String ADD_NOTIFICATION_SUCCESS= "008";
    public static final String ADD_NOTIFICATION_SUCCESSS_MSG= "添加通知成功";

    public static final String ADD_NOTIFICATION_FAIL= "009";
    public static final String ADD_NOTIFICATION_FAIL_MSG= "添加通知失败";
}
