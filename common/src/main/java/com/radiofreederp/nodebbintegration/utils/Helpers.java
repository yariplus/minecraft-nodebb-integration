package com.radiofreederp.nodebbintegration.utils;

import java.util.HashMap;
import java.util.Map;

public class Helpers {

    // TODO: There's some functional way to do this. Find an adult.
    public static String replaceMap (String message, HashMap<String, String> vars) {
        for (Map.Entry<String, String> entry : vars.entrySet()) {
            message = message.replaceAll(entry.getKey(), entry.getValue());
        }
        return message;
    }
}
