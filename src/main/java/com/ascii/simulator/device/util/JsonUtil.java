package com.ascii.simulator.device.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {
    public static JSONObject convertStringToJson(String jsonString) throws Exception {
        return new JSONObject(jsonString);
    }

    public static JSONArray convertStringToJsonAray(String jsonString) throws Exception {
        return new JSONArray(jsonString);
    }
}
