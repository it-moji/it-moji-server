package com.itmoji.itmojiserver.api.v1.attendance.dto;

import java.util.List;

public class BadgeWithConditionsDTO {
    private Long id;
    private String icon;
    private String name;
    private List<ConditionGroupDTO> conditionGroups;

    public BadgeWithConditionsDTO(Long id, String icon, String name, List<ConditionGroupDTO> conditionGroups) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.conditionGroups = conditionGroups;
    }

    public Long getId() {
        return id;
    }
    public String getIcon() {
        return icon;
    }
    public String getName() {
        return name;
    }
    public List<ConditionGroupDTO> getConditionGroups() {
        return conditionGroups;
    }
}
