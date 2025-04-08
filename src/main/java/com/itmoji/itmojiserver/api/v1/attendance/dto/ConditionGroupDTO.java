package com.itmoji.itmojiserver.api.v1.attendance.dto;

import java.util.List;

public class ConditionGroupDTO {
    private Long groupId;
    private List<ConditionDTO> conditions;

    public ConditionGroupDTO(Long groupId, List<ConditionDTO> conditions) {
        this.groupId = groupId;
        this.conditions = conditions;
    }

    public Long getGroupId() {
        return groupId;
    }
    public List<ConditionDTO> getConditions() {
        return conditions;
    }
}
