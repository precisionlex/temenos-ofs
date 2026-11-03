package com.precisionlex;

/**
 * Represents the user information section of an OFS message.
 * <p>
 * Serialized as a slash-delimited positional string:
 * {@code USER_ID/PASSWORD/COMPANY/FIELD4/FIELD5/REPLACE}
 * Trailing empty segments are omitted; empty middle segments are preserved.
 * </p>
 */
public class OfsUserInformation {

    private String userId;
    private String password;
    private String company;
    private String field4;
    private String field5;
    private boolean replace;

    public OfsUserInformation() {
    }

    public OfsUserInformation(String userId, String password, String company,
                              String field4, String field5, boolean replace) {
        this.userId = userId;
        this.password = password;
        this.company = company;
        this.field4 = field4;
        this.field5 = field5;
        this.replace = replace;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    /**
     * Serializes to the OFS positional format: {@code USER/PASSWORD/COMPANY/F4/F5/REPLACE}.
     * Null fields become empty segments; trailing empty segments are dropped.
     */
    @Override
    public String toString() {
        String[] parts = {userId, password, company, field4, field5, replace ? "1" : ""};

        int end = parts.length;
        while (end > 0 && (parts[end - 1] == null || parts[end - 1].isEmpty())) {
            end--;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < end; i++) {
            if (i > 0) {
                sb.append('/');
            }
            if (parts[i] != null) {
                sb.append(parts[i]);
            }
        }
        return sb.toString();
    }
}
