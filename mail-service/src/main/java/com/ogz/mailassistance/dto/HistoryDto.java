package com.ogz.mailassistance.dto;

public class HistoryDto {
    private String historyId;

    public HistoryDto(String historyId) {
        this.historyId = historyId;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }
}
