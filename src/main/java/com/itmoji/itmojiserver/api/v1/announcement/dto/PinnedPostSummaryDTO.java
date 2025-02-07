package com.itmoji.itmojiserver.api.v1.announcement.dto;

import com.itmoji.itmojiserver.api.v1.announcement.Post;
import java.time.LocalDateTime;

public record PinnedPostSummaryDTO(
        Long id,
        String title,
        String postCategory,
        LocalDateTime createdAt,
        Long viewCount
) {
    public PinnedPostSummaryDTO(Post post) {
        this(
                post.getId(),
                post.getTitle(),
                post.getPostCategory().name(),
                post.getCreatedAt(),
                post.getViewCount()
        );
    }
}
