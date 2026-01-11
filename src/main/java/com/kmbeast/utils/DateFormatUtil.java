package com.kmbeast.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatUtil {

    private static final DateTimeFormatter CHINESE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MM月dd日");

    /**
     * 将 "07月15日" 格式的字符串转换为 LocalDate
     */
    public static LocalDate parseChineseDate(String dateStr) {
        try {
            // 获取当前年份
            int currentYear = LocalDate.now().getYear();
            // 拼接年份后解析
            return LocalDate.parse(currentYear + "年" + dateStr,
                    DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("日期格式不正确，应为'MM月dd日'格式: " + dateStr);
        }
    }

    /**
     * 将 LocalDate 格式化为 "07月15日" 格式
     */
    public static String formatToChineseDate(LocalDate date) {
        return date.format(CHINESE_DATE_FORMATTER);
    }
}