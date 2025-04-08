package com.itmoji.itmojiserver.api.v1.attendance.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ConditionGroupWithoutIdDTO {
    private List<BadgeConditionDTO> conditions;
}
