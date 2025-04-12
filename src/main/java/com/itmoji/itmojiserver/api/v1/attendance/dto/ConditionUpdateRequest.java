package com.itmoji.itmojiserver.api.v1.attendance.dto;

import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOptions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConditionUpdateRequest(
        @NotBlank(message = "key는 필수 입력 값입니다.") AttendanceOptions key,
        @NotBlank(message = "detailKeyId는 필수 입력 값입니다.") Long detailKeyId,
        @NotNull(message = "count는 필수 입력 값입니다.") Integer count,
        @NotBlank(message = "range는 필수 입력 값입니다.") String range
) {
}
