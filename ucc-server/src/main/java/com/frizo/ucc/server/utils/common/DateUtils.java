package com.frizo.ucc.server.utils.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static Instant stringToStartOfDay(String arg){
        LocalDate localDate = LocalDate.parse(arg, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    public static Instant stringToEndOfDay(String arg){
        LocalDate localDate = LocalDate.parse(arg, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        localDate = localDate.plusDays(1);
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}
