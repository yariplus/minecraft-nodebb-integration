package com.radiofreederp.nodebbintegration.utils;

import java.util.Map;

/**
 * Created by Yari on 5/2/2016.
 */
public interface Helpers {

    // TODO: There's some functional way to do this. Find an adult.
    static String replaceMap(String message, Map<String, String> vars){
        for (Map.Entry<String, String> entry : vars.entrySet()) {
            message = message.replaceAll(entry.getKey(), entry.getValue());
        }
        return message;
    }
}
