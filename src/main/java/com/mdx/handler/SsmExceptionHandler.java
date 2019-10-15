package com.mdx.handler;

import com.mdx.exception.SsmException;
import com.mdx.vo.ResultVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class SsmExceptionHandler {

    @ExceptionHandler({SsmException.class})
    @ResponseBody
    public ResultVo ssmException(SsmException ex){
        return new ResultVo(ex.getCode(),ex.getMessage(),null);
    }


}
