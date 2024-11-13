package com.choongang.frombirth_backend.util;

public class Util {
    public static String getRecordFileName(Integer recordId, String photoURL) {
        return "record" + "/" + recordId + "/" + photoURL;
    }
}
