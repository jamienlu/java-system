package com.asura.database.util;

import org.apache.logging.log4j.util.Strings;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String parseStr(Date date, String format) {
        if (Strings.isEmpty(format)) {
            format = DEFAULT_FORMAT;
        }
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat (format); // "yyyy-MM-dd HH:mm:ss"
            result = sdf.format (date);
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        return result;
    }

    public static String parseStr(long date, String format) {
        return parseStr(new Date(date), format);
    }
}
