package com.itmoji.itmojiserver.api.v1.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ConditionCreateRequest {
    @NotNull(message = "배지 id는 필수 입력 값입니다.")
    private Long badgeId;

    @NotNull(message = "조건 그룹 ID는 필수 입력 값입니다.")
    private Long badgeConditionGroupId;

    @NotBlank(message = "조건 key는 필수 입력 값입니다.")
    private String key;

    @NotNull(message = "count는 필수 입력 값입니다.")
    private Integer count;

    @NotBlank(message = "range는 필수 입력 값입니다.")
    private String range;

    public void setBadgeId(final Long badgeId) {
        this.badgeId = badgeId;
    }
}
