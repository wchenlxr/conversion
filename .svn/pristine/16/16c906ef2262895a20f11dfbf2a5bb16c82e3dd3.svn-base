package com.adtech.cn.aspect.support;

import com.adtech.cn.exception.ApplicationException;
import com.adtech.cn.exception.SystemError;
import com.adtech.cn.exception.support.IBaseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 返回异常
 * Created by Administrator on 2018/3/22.
 */
public class RestResult {
    private static final Logger logger = LoggerFactory.getLogger(RestResult.class);

    private String code;

    private String message;

    private Object stack;

    private Object body;

    private RestResult(String code) {
        this.code = code;
    }

    /**
     * 快速OK
     *
     * @return
     */
    public static RestResult Ok() {
        return Ok(null);
    }

    /**
     * 返回成功
     *
     * @param body
     * @return
     */
    public static RestResult Ok(Object body) {
        RestResult rr = new RestResult("OK");
        rr.setBody(body);
        return rr;
    }

    /**
     * 返回错误
     *
     * @param error
     * @param stack
     * @return
     */
    public static RestResult Fail(IBaseError error, Object stack) {
        if (error == null || error.getCode() == null || error.getCode().equals("") || error.getCode().equals("OK")) {
            throw new ApplicationException(SystemError.SYSTEM_ERROR);
        }
        RestResult rr = new RestResult(error.getCode());
        rr.setCode(error.getCode());
        rr.setMessage(error.getText());
        if (logger.isDebugEnabled()) {
            rr.setStack(stack);
        }
        return rr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getStack() {
        return stack;
    }

    public void setStack(Object stack) {
        this.stack = stack;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
