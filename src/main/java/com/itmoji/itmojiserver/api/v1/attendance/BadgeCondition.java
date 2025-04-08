package com.itmoji.itmojiserver.api.v1.attendance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BadgeCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long badgeConditionGroupId;

    @Enumerated(EnumType.STRING)
    private AttendanceOptions conditionKey;

    @Column(name = "detail_key_id")
    private Long detailKeyId;

    private int count;

    @Enumerated(EnumType.STRING)
    private Range conditionRange;

    public enum Range {
        MORE, LESS
    }

    @Builder
    public BadgeCondition(Long badgeConditionGroupId, AttendanceOptions key, Long detailKeyId, int count, Range range) {
        this.badgeConditionGroupId = badgeConditionGroupId;
        this.conditionKey = key;
        this.detailKeyId = detailKeyId;
        this.count = count;
        this.conditionRange = range;
    }

    public void update(AttendanceOptions key, Long detailKeyId, int count, Range range) {
        this.conditionKey = key;
        this.detailKeyId = detailKeyId;
        this.count = count;
        this.conditionRange = range;
    }
}
