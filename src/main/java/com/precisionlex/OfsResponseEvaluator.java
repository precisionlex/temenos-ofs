package com.precisionlex;

public class OfsResponseEvaluator {

    public static boolean isSessionExpired(String ofsResponse) {
        return ofsResponse != null &&
                (ofsResponse.contains("TAFJERR-1060: Session Expiration") || ofsResponse.equals("-1"));
    }

    public static boolean isLogicalError(String ofsResponse) {
        return ofsResponse != null && ofsResponse.contains("/-1");
    }
}
