package com.itmoji.itmojiserver.api.v1.attendance.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class BadgeWithConditionsForUpdateDTO {
    private String icon;
    private String name;
    private List<BadgeConditionGroupDTO> conditionGroups;
}
