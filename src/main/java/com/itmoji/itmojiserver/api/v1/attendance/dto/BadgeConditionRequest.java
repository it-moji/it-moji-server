package com.itmoji.itmojiserver.api.v1.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BadgeConditionRequest {

    @NotBlank(message = "조건 key는 필수 입력 값입니다.")
    private String key;

    @NotNull(message = "count는 필수 입력 값입니다.")
    private Integer count;

    @NotBlank(message = "range는 필수 입력 값입니다.")
    private String range;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public String getRange() {
        return range;
    }
    public void setRange(String range) {
        this.range = range;
    }
}
