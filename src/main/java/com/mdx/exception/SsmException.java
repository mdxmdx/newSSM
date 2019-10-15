package com.mdx.exception;

import com.mdx.enums.ExceptionInfoEnum;
import lombok.Data;

@Data
public class SsmException extends RuntimeException {

    private Integer code;

    public SsmException(Integer code,String message) {
        super(message);
        this.code = code;
    }
    public SsmException(ExceptionInfoEnum enums) {
        super(enums.getMsg());
        this.code = enums.getCode();
    }
}
