package com.adtech.cn.exception;

import com.adtech.cn.exception.support.IBaseError;

/**
 * 异常返回类型定义,可以在此扩展也可以重新定义枚举实现
 * Created by Administrator on 2018/3/22.
 */
public enum SystemError implements IBaseError {
    DB_EXCEPTION("db_error01", ""),
    SYSTEM_ERROR("system_error", "系统异常"),
    SYSTEM_OTHER_ERROR("other_error", "其它异常"),
    DB_CANNOT_REPEAT("DB_CANNOT_REPEAT","业务厂商名称不能重复"),
    EXCEL_ERROR("EXCEL_ERROR","excel模版解析出重");
    private String code;
    private String text;

    SystemError(String code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }
}
