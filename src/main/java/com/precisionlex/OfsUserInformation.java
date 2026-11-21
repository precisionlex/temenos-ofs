package com.precisionlex;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents the user information section of an OFS message.
 * <p>
 * Serialized as a slash-delimited positional string:
 * {@code USER_ID/PASSWORD/COMPANY/OFS_ALIAS/USER_ALIAS_PASSWORD/REPLACE}
 * Trailing empty segments are omitted; empty middle segments are preserved.
 * </p>
 */
public class OfsUserInformation {

    private String userId;
    private String password;
    private String company;
    private String ofsAlias;
    private String userAliasPassword;
    private boolean replace;

    public OfsUserInformation() {
    }

    public OfsUserInformation(String userId, String password, String company,
                              String ofsAlias, String userAliasPassword, boolean replace) {
        this.userId = userId;
        this.password = password;
        this.company = company;
        this.ofsAlias = ofsAlias;
        this.userAliasPassword = userAliasPassword;
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

    public String getOfsAlias() {
        return ofsAlias;
    }

    public void setOfsAlias(String ofsAlias) {
        this.ofsAlias = ofsAlias;
    }

    public String getUserAliasPassword() {
        return userAliasPassword;
    }

    public void setUserAliasPassword(String userAliasPassword) {
        this.userAliasPassword = userAliasPassword;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    /**
     * Serializes to the OFS positional format: {@code USER/PASSWORD/COMPANY/OFS_ALIAS/USER_ALIAS_PASSWORD/REPLACE}.
     * Null fields become empty segments; trailing empty segments are dropped.
     */
    @Override
    public String toString() {
        String[] fields = {userId, password, company, ofsAlias, userAliasPassword, replace ? "1" : ""};
        int fieldCount = fields.length;
        while (fieldCount > 0 && StringUtils.isEmpty(fields[fieldCount - 1])) {
            fieldCount--;
        }
        return StringUtils.join(fields, '/', 0, fieldCount);
    }
}
