package com.xailab.vehicle.xaicommon.utils;
import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";

    private static final Random random = new SecureRandom();

    public static int generateInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static char generateRandomChar() {
        String chars = LOWER_CASE + UPPER_CASE + NUMBERS;
        int index = random.nextInt(chars.length());
        return chars.charAt(index);
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(generateRandomChar());
        }
        return sb.toString();
    }

    public static char generateRandomCharNumber() {
        String chars = NUMBERS;
        int index = random.nextInt(chars.length());
        return chars.charAt(index);
    }

    public static String generateRandomNumberString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(generateRandomCharNumber());
        }
        return sb.toString();
    }
}
