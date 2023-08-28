package com.example.jdnc_library.util;

import java.util.regex.Pattern;

public class RegexUtil {

    private static String EIGHT_NUMBER_REGEX = "^[0-9]{8}$";

    public static boolean isEightNumber(String number) {
        return Pattern.matches(EIGHT_NUMBER_REGEX, number);
    }

}
