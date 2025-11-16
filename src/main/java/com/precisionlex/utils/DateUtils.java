package com.precisionlex.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private DateUtils() {
        // prevent instantiation
    }

    public static String getCurrentDateTime() {
        return formatDateTime("yyyy-MM-dd'T'HH:mm:ss");
    }

    public static String getCurrentDateForId() {
        return formatDateTime("yyMMdd");
    }

    public static String getCurrentTimestamp() {
        return formatDateTime("yyyyMMddHHmmss");
    }

    private static String formatDateTime(String pattern) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTimeNow.format(formatter);
    }
}
