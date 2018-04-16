package com.adtech.cn.exception;


import com.adtech.cn.exception.support.IBaseError;

/**
 * 应用异常处理
 */
public class ApplicationException extends RuntimeException {
    private IBaseError error;

    public ApplicationException(IBaseError error) {
        super(error.getCode() + ": " + error.getText());
        this.error = error;
    }

    public IBaseError getError() {
        return error;
    }

    public void setError(IBaseError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return error.getCode() + ": " + error.getText();
    }
}
