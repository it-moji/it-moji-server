package com.itmoji.itmojiserver.api.v1.announcement.dto;

import com.itmoji.itmojiserver.api.v1.announcement.Post;
import java.util.Objects;


public record RelatedDTO(
        RelatedPostDTO prev,
        RelatedPostDTO next
) {
    public RelatedDTO(Post previousPost, Post nextPost) {
        this(
                Objects.isNull(previousPost) ? null : new RelatedPostDTO(previousPost),
                Objects.isNull(nextPost) ? null : new RelatedPostDTO(nextPost)
        );
    }
}
