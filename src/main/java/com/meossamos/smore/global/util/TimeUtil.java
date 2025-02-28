package com.meossamos.smore.global.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtil {
    public static long getCurrentTimeMillis() {
        return LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
