package com.itmoji.itmojiserver.api.v1.announcement.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostCreateRequestDTO(
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @NotBlank(message = "글 내용 작성은 필 수 입니다.")
        String content,

        @NotBlank(message = "카테고리 선택은 필수입니다.")
        String postCategory,

        boolean isPinned) {
}
