package com.precisionlex;

import java.util.ArrayList;
import java.util.List;

public class OfsCsmResponse {
    private int successfulEntriesCount;
    private int suspendedEntriesCount;
    private int failedEntriesCount;

    private List<String> successfulEntries = new ArrayList<>();
    private List<String> suspendedEntries = new ArrayList<>();
    private List<String> failedEntries = new ArrayList<>();

    public int getSuccessfulEntriesCount() {
        return successfulEntriesCount;
    }

    public void setSuccessfulEntriesCount(int successfulEntriesCount) {
        this.successfulEntriesCount = successfulEntriesCount;
    }

    public int getSuspendedEntriesCount() {
        return suspendedEntriesCount;
    }

    public void setSuspendedEntriesCount(int suspendedEntriesCount) {
        this.suspendedEntriesCount = suspendedEntriesCount;
    }

    public int getFailedEntriesCount() {
        return failedEntriesCount;
    }

    public void setFailedEntriesCount(int failedEntriesCount) {
        this.failedEntriesCount = failedEntriesCount;
    }

    public boolean isSuccess() {
        return successfulEntriesCount > 0
                && suspendedEntriesCount == 0
                && failedEntriesCount == 0;
    }

    public boolean isSuspended() {
        return suspendedEntriesCount > 0;
    }
 
    public boolean isError() {
        return failedEntriesCount > 0;
    }

    public List<String> getSuccessfulEntries() {
        return successfulEntries;
    }

    public void setSuccessfulEntries(List<String> successfulEntries) {
        this.successfulEntries = successfulEntries;
    }

    public List<String> getSuspendedEntries() {
        return suspendedEntries;
    }

    public void setSuspendedEntries(List<String> suspendedEntries) {
        this.suspendedEntries = suspendedEntries;
    }

    public List<String> getFailedEntries() {
        return failedEntries;
    }

    public void setFailedEntries(List<String> failedEntries) {
        this.failedEntries = failedEntries;
    }
}
