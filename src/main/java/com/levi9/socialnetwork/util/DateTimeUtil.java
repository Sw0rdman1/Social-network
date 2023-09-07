package com.levi9.socialnetwork.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final String PATTERN = "yyyy-MM-dd HH:mm";

    public static String formatDateTime(LocalDateTime dateTime) {
        return formatDateTime(dateTime, PATTERN);
    }

    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }
}
