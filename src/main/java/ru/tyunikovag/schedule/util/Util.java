package ru.tyunikovag.schedule.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static final String regForFIO = "[а-яА-Я]+\\s[а-яА-Я]\\.\\s?[а-яА-Я]\\.";
    private static Pattern patternFIO = Pattern.compile(regForFIO);
    public static final String WRONG_FIO = "неверный формат Ф.И.О.";

    public static String getFIO(String line) {
        Matcher matcher = patternFIO.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return WRONG_FIO;
        }
    }
}
