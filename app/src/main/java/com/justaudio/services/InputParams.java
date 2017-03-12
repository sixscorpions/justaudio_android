package com.justaudio.services;

import android.support.v4.util.ArrayMap;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ${VIDYA}
 */

public class InputParams {


    /*
    * REPLACE THE BELOW METHOD WITH YOUR REQUIRED FIELDS
    * */
    public static JSONObject getLoginParams(String userName, String deviceType) {
        HashMap<String, String> innerMap = new HashMap<>();
        innerMap.put("email", userName);
        innerMap.put("password", deviceType);
        return new JSONObject(innerMap);
    }


    /*
    *  SET THE LIST OF HEADER TO THE MAP
    * */
    public static ArrayMap<String, String> getHeaderParam() {
        ArrayMap<String, String> headerMap = new ArrayMap<>();
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Accept-Language", "en-US");
        headerMap.put("Content-Language", "en-US");
        return headerMap;
    }

}
