package com.example.demo.commom;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xin
 * @since 2023/3/8 9:43
 */
public class WebExceptionAdvice {
    @ExceptionHandler(RuntimeException.class)
    public R handleRuntimeException(RuntimeException e) {
        return R.error().message("服务器异常");
    }

    //自定义异常
    @ExceptionHandler(ZException.class)
    @ResponseBody //为了返回数据
    public R error(ZException e) {
        e.printStackTrace();

        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
