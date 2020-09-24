package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.service.base.result.ResultCodeEnum;
import lombok.Data;

@Data
public class GuliException extends RuntimeException {

    // 扩展字段：错误码
    private Integer code;

    // 扩展字段:module
    private String  module;

    public GuliException(ResultCodeEnum resultCodeEnum,String module) {
        super(resultCodeEnum.getMessage());
        this.module=module;
        this.code = resultCodeEnum.getCode();
    }

    /**
     * 接受状态码和消息
     * @param code
     * @param message
     */
    public GuliException(Integer code, String message) {
        super(message);
        this.code=code;
    }

    /**
     * 接收枚举类型
     * @param resultCodeEnum
     */
    public GuliException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", module='" + module + '\'' +
                '}';
    }
}
