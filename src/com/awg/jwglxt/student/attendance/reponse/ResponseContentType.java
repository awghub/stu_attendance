package com.awg.jwglxt.student.attendance.reponse;

import java.util.HashMap;

/**
 * 
 * 定义一个统一的请求返回数据类型
 * 
 * @author AWG
 *
 */

public class ResponseContentType {
    
    public static final String FLAG_SUCCESS = "success";
    public static final String FLAG_FAIL = "fail";
    
    // 请求标识,有"success"和"fail"两种
    // 若flag="success",则data内返回前端需要的JSON数据；
    // 若flag="fail",则data内返回null
    private String flag;
    // 返回的提示信息码及提示信息文本
    private HashMap<String, String> respObj;
    // 返回给前端的数据
    private Object data;

    // 定义通用的创建方法
    // 1.默认为请求成功的创建方法---->调用通用的创建方法
    public static ResponseContentType create(HashMap<String, String> respObj, Object result) {
        return ResponseContentType.create(respObj, result, ResponseContentType.FLAG_SUCCESS);
    }

    // 2.通用的创建方法
    public static ResponseContentType create(HashMap<String, String> respObj, Object result, String flag) {
        ResponseContentType crt = new ResponseContentType();
        crt.setFlag(flag);
        crt.setRespObj(respObj);
        crt.setData(result);
        return crt;
    }

    public HashMap<String, String> getRespObj() {
        return respObj;
    }

    public void setRespObj(HashMap<String, String> respObj) {
        HashMap<String, String> respObjMap = new HashMap<String, String>();
        respObjMap.put("respObjCode", respObj.get("respObjCode"));
        respObjMap.put("respObjMsg", respObj.get("respObjMsg"));
        this.respObj = respObjMap;
    }

    public String getFlag() {
        return flag;
    }
    
    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
