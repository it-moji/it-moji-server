package com.itmoji.itmojiserver.api.v1.attendance.dto;

import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOptions;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeConditionDTO {
    private AttendanceOptions key;
    private long detailKeyId;
    private int count;
    private String range;
}
