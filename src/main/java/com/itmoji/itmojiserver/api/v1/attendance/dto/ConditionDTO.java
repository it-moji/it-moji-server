package com.itmoji.itmojiserver.api.v1.attendance.dto;

public record ConditionDTO(Long id, String key, Long detailKeyId, int count, String range) {
}
