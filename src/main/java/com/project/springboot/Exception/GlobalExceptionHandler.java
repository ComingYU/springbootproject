package com.project.springboot.Exception;

import com.project.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 抛出ServiceException异常时，返回错误信息
     * @param e 业务异常
     * @return
     */

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException e){
        return Result.error(e.getCode(),e.getMessage());
    }
}
