package ru.saruat.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {
    public static <T extends Class<?>> Logger getLogger(Class<T> clazz) {
        return LogManager.getLogger(clazz);
    }
}