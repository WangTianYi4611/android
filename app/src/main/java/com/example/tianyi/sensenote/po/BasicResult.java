package com.example.tianyi.sensenote.po;

import java.util.List;

public class BasicResult<T> {

    public static final int SUCCESS = 200; //成功
    public static final int FAIL = 500;//失败

    private int code = SUCCESS; //状态码

    private String msg = "请求成功"; // 信息

    private T singleResult; //单个结果值得时候返回这个

    private List<T> multipleResult; //多个结果值


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getSingleResult() {
        return singleResult;
    }

    public void setSingleResult(T singleResult) {
        this.singleResult = singleResult;
    }

    public List<T> getMultipleResult() {
        return multipleResult;
    }

    public void setMultipleResult(List<T> multipleResult) {
        this.multipleResult = multipleResult;
    }

    @Override
    public String toString() {
        return "BasicResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", singleResult=" + singleResult +
                ", multipleResult=" + multipleResult +
                '}';
    }
}
