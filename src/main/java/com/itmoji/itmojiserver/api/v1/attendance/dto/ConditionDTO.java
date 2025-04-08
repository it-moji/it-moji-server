package com.itmoji.itmojiserver.api.v1.attendance.dto;

public class ConditionDTO {
    private Long id;
    private String key;
    private int count;
    private String range; // "MORE" 또는 "LESS"

    public ConditionDTO(Long id, String key, int count, String range) {
        this.id = id;
        this.key = key;
        this.count = count;
        this.range = range;
    }

    public Long getId() {
        return id;
    }
    public String getKey() {
        return key;
    }
    public int getCount() {
        return count;
    }
    public String getRange() {
        return range;
    }
}
