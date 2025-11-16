package com.precisionlex.utils;

import java.util.IllegalFormatException;
import java.util.Locale;

public final class NumberUtils {

    private NumberUtils() {
        // prevent instantiation
    }

    public static String numberToReference(long number) {
        String numbers = "0123456789";
        String letters = "ABCDEFGHJKLMNPQRSTVWXYZ";
        String referenceChars = numbers + letters;
        StringBuilder reference = new StringBuilder();

        int base = letters.length() + numbers.length();

        do {
            long remainder = number % base;
            number = number / base;
            reference.append(referenceChars.charAt((int) remainder));
        } while (number > 0);

        return reference.reverse().toString();
    }

    public static String numberToReference(long number, int length) {
        String reference = numberToReference(number);
        // simple left pad with zeros
        while (reference.length() < length) {
            reference = "0" + reference;
        }
        // trim to rightmost length characters
        return reference.substring(reference.length() - length);
    }

    public static double stringToDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String doubleToString(double value) {
        try {
            return String.format(Locale.US, "%.2f", value);
        } catch (IllegalFormatException e) {
            return "";
        }
    }

    public static String doubleToAmount(double value) {
        return String.format("%.2f", roundByDecimalPlaces(value));
    }

    public static String doubleToAmount(double value, int places) {
        return String.format("%.2f", roundByDecimalPlaces(value, places));
    }

    public static double roundByDecimalPlaces(double value) {
        return roundByDecimalPlaces(value, 2);
    }

    public static double roundByDecimalPlaces(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        return Math.round(value * Math.pow(10, places)) / Math.pow(10, places);
    }
}
