package com.founder.econdaily.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

public class DateParseUtil {


    public static final String DATE_STRICK = "yyyy-MM-dd";
    public static final String DATETIME_STRICK = "yyyy-MM-dd hh:mm:ss";
    public static final String DATE_OBLIQUE = "yyyy/MM/dd";

    public static String dateToStringWithSplit(Date date) {
        if (date == null) {
            return null;
        }
        return stringSplit(dateToString(date));
    }

    public static String dateToStringWithSplit(Date date, String format) {
        return stringSplit(dateToString(date, format));
    }


    public static String dateToString(Date date, String format) {
        if (date == null) {
            return null;
        }
        if (StringUtils.isEmpty(format)) {
            format = DATE_STRICK;
        }
        return DateFormatUtils.format(date, format);
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        return dateToString(date, DATE_STRICK);
    }

    public static String stringSplit(String word) {
        if (StringUtils.isEmpty(word)) {
            return null;
        }
        return word.replaceAll(RegxUtil.getMatcher(word, RegxUtil.REG_DIGITAL).replaceAll("").substring(0,1), RegxUtil.REG_Blank);
    }


    public static Date stringToDate(String dateStr) throws ParseException {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        return DateUtils.parseDate(dateStr, new String[]{"yyyy-MM-dd"});
    }
}
