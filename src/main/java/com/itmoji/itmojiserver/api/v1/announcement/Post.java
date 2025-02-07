package com.itmoji.itmojiserver.api.v1.announcement;

import com.itmoji.itmojiserver.api.v1.utils.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Post extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private Long viewCount = 0L;

    @Column(nullable = false)
    private boolean isPinned;

    public void update(String title, String content, PostCategory postCategory, boolean isPinned) {
        this.title = title;
        this.content = content;
        this.postCategory = postCategory;
        this.isPinned = isPinned;
    }
}
