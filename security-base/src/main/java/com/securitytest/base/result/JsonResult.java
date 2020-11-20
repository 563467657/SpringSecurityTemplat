package com.securitytest.base.result;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class JsonResult {
	// 响应业务状态
    private Integer code;

    // 响应消息
    private String message;

    // 响应中的数据
    private Object data;
	
	public JsonResult() {
		
    }
	public JsonResult(Object data) {
        this.code = 200;
        this.message = "OK";
        this.data = data;
    }
    
    public JsonResult(String message, Object data) {
        this.code = 200;
        this.message = message;
        this.data = data;
    }

    public JsonResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static JsonResult ok() {
        return new JsonResult(null);
    }
    public static JsonResult ok(String message) {
        return new JsonResult(message, null);
    }
    public static JsonResult ok(Object data) {
        return new JsonResult(data);
    }
    public static JsonResult ok(String message, Object data) {
        return new JsonResult(message, data);
    }

    public static JsonResult build(Integer code, String message) {
        return new JsonResult(code, message, null);
    }

    public static JsonResult build(Integer code, String message, Object data) {
        return new JsonResult(code, message, data);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }


    /**
     * JSON字符串转成 JsonResult 对象
     * @param json
     * @return
     */
    public static JsonResult format(String json) {
        try {
            return JSON.parseObject(json, JsonResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
