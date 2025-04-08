package com.itmoji.itmojiserver.api.v1.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ConditionUpdateRequest {
    @NotBlank(message = "조건 key는 필수 입력 값입니다.")
    private String key;

    @NotNull(message = "count는 필수 입력 값입니다.")
    private Integer count;

    @NotBlank(message = "range는 필수 입력 값입니다.")
    private String range;
}
