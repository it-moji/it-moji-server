package com.itmoji.itmojiserver.api.v1.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BadgeConditionRequest(
        @NotBlank(message = "key는 필수 입력 값입니다.") String key,
        @NotBlank(message = "detailKey는 필수 입력 값입니다.") Long detailKeyId,
        @NotNull(message = "count는 필수 입력 값입니다.") Integer count,
        @NotBlank(message = "range는 필수 입력 값입니다.") String range
) {
}
