package com.itmoji.itmojiserver.api.v1.announcement.dto;

import com.itmoji.itmojiserver.api.v1.announcement.Post;
import java.time.LocalDateTime;

public record RelatedPostDTO(
        Long id,
        String title,
        LocalDateTime createdAt
) {
    public RelatedPostDTO(Post post) {
        this(post.getId(), post.getTitle(), post.getCreatedAt());
    }
}
