package com.mdx.enums;

import lombok.Getter;

@Getter
public enum ExceptionInfoEnum {
    SUCCESS(0,"成功"),
    PARAM_ERROR(1,"参数不合法"),
    CHECKUSERNAME_ERROR(21,"用户名校验失败"),
    REGISTER_ERROR(22,"注册失败"),
    LOGIN_USERNAME_ERROR(23,"用户名不存在！"),
    LOGIN_PASSWORD_ERROR(24,"密码错误！"),
    CHECK_BY_NAME_ERROR(31,"未查询出结果！"),
    CHECK_BY_ID_ERROR(32,"根据id查询商品信息失败"),
    ITEM_ADD_ERROR(41,"添加商品失败"),
    ITEM_ADD_OVERSIZE(42,"图片大小过大！"),
    ITEM_ADD_TYPE_ERROR(43,"图片类型错误！"),
    ITEM_ADD_DAMAGED_ERROR(44,"图片已损坏！"),
    ITEM_DELETE_ERROR(45,"删除商品失败！"),
    UPDATE_ERROR(51,"修改商品失败！");


    private Integer code;
    private String msg;

    ExceptionInfoEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
