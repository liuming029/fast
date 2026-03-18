package com.fast.system.exception;

import com.fast.system.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author huacai
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理RuntimeException异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e) {
        logger.error("运行时异常: ", e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 处理Exception异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        logger.error("系统异常: ", e);
        return AjaxResult.error(e.getMessage());
    }
}