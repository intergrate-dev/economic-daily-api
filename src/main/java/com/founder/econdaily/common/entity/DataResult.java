package com.founder.econdaily.common.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DataResult extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 364922749340917897L;

    /**
     * 响应状态码
     */
    protected int status;

    /**
     * 响应消息
     */
    protected String message = "";

    private Object data;

    public DataResult() {

    }

    public static DataResult error(String message) {
        return error(500, message);
    }

    public static DataResult error(int state, String message) {
        DataResult dr = new DataResult();
        dr.put("state", state);
        dr.put("message", message);
        return dr;
    }

    public static DataResult ok(String message) {
        DataResult dr = new DataResult();
        dr.put("message", message);
        return dr;
    }

    public static DataResult ok(Map<String, Object> map) {
        DataResult dr = new DataResult();
        dr.putAll(map);
        return dr;
    }

    @Override
    public DataResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public DataResult(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public DataResult(int status, String message, Object data) {

        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "status: " + status +
                ", message: '" + message + '\'' +
                '}';
    }
}
