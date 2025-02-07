package com.itmoji.itmojiserver.api.v1.announcement.dto.request;

public record PostSearchRequest(
        String query,
        String type,
        Integer page,
        Integer size
) {
    public PostSearchRequest {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size <= 0) {
            size = 3;
        }
    }
}
