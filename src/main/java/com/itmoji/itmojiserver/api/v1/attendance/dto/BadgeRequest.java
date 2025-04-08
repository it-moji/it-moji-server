package com.itmoji.itmojiserver.api.v1.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;

@Getter
public class BadgeRequest {

    @NotBlank(message = "아이콘은 필수 입력 값입니다.")
    private String icon;

    @NotBlank(message = "배지 이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "조건 옵션은 필수 입력 값입니다.")
    private List<List<BadgeConditionRequest>> conditionGroups;
}
