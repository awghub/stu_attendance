package com.awg.jwglxt.student.attendance.util;

import java.util.HashMap;
import com.awg.jwglxt.student.attendance.reponse.ResponseContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * JSON生成工具类
 * 
 * @author AWG
 * @version 2.0
 * 
 * 2019-05-31
 */
public class ResultJSONGenerateUtil {

    /**
     * 将某个处理结果转化为JSON
     * @param flag 请求的状态标识,success或fail,不区分大小写
     * @param respObjCode 响应的提示的信息的状态码,默认为"00000"
     * @param respObjMsg 响应的提示的信息的文本,默认为"null"
     * @param resultObj 需要进行转换的对象,可以为空
     * @return 需要进行转换的对象经过转换的得到的JSON字符串
     * @throws JsonProcessingException JSON解析异常
     */
    public static String object2JSON(String flag, String respObjCode, String respObjMsg, Object resultObj) throws JsonProcessingException {
        if (flag == null || !flag.equalsIgnoreCase(ResponseContentType.FLAG_SUCCESS) || !flag.equalsIgnoreCase(ResponseContentType.FLAG_FAIL)) {
            flag = ResponseContentType.FLAG_SUCCESS;
        }
        if (respObjCode == null || respObjMsg == null || respObjCode.length() == 0 || respObjMsg.length() == 0) {
            respObjCode = "00000";
            respObjMsg = "null";
        }
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> reqStatusMap = new HashMap<String,String>();
        reqStatusMap.put("respObjCode",respObjCode);
        reqStatusMap.put("respObjMsg",respObjMsg);
        ResponseContentType rct = ResponseContentType.create(reqStatusMap, resultObj, flag);
        return mapper.writeValueAsString(rct);
    }
}
