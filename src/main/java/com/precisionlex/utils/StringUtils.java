package com.precisionlex.utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class StringUtils {

    private StringUtils() {
        // prevent instantiation
    }

    public static String toLatin(String str) {
        if (str == null) return "";
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        return nfdNormalizedString.replaceAll("[^\\p{ASCII}]", "");
    }

    public static String replaceTextChars(String elm) {
        if (elm == null || elm.isEmpty() || "-".equals(elm)) {
            elm = "NA";
        }

        Map<String, String> replacements = new HashMap<>();
        replacements.put("|", "%|%");
        replacements.put("\"", "\"|\"");
        replacements.put("?", "%?%");
        replacements.put(",", "\"?\"");
        replacements.put("_", "'_'");
        replacements.put("^", "%^%");
        replacements.put("/", "\"^\"");

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            elm = elm.replace(entry.getKey(), entry.getValue());
        }

        return elm;
    }

    public static List<String> splitLine(String text, int partSize) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        List<String> parts = new ArrayList<>();
        for (int i = 0; i < text.length(); i += partSize) {
            parts.add(text.substring(i, Math.min(text.length(), i + partSize)));
        }
        return parts;
    }

    public static String removeNonAlphanumericChars(String text) {
        if (text == null) return "";
        return text.replaceAll("[^a-zA-Z0-9\\s]", "");
    }

    public static String substring(String str, int start, int end) {
        if (str == null) return "";
        if (start < 0) start = 0;
        if (end > str.length()) end = str.length();
        return str.substring(start, end);
    }
}
