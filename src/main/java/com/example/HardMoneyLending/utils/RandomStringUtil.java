package com.example.HardMoneyLending.utils;

import java.util.Random;

public class RandomStringUtil {

    private static final int LEFT_LIMIT = 48; // numeral '0'
    private static final int RIGHT_LIMIT = 122; // letter 'z'
    private static final int TARGET_STRING_LENGTH = 10;

    public static final String getRandomAlphaNumericID(String withPrefix, String withPostfix) {
        Random random = new Random();
        String id = random.ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <=90 || i >= 97))
                .limit(TARGET_STRING_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return (withPrefix == null || withPrefix.isEmpty() ? "" : withPrefix)
                + id
                + (withPostfix == null || withPostfix.isEmpty() ? "" : withPostfix);
    }


}
