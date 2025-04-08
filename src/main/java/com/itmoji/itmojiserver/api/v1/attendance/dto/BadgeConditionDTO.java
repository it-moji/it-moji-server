package com.itmoji.itmojiserver.api.v1.attendance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeConditionDTO {
    private String key;
    private int count;
    private String range;
}
