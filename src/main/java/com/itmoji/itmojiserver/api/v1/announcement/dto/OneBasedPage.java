package com.itmoji.itmojiserver.api.v1.announcement.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record OneBasedPage<T>(
        List<T> content,
        int totalPages,
        int currentPage,
        long totalElements
) {
    public OneBasedPage(Page<T> page) {
        this(page.getContent(), page.getTotalPages(), page.getNumber() + 1, page.getTotalElements());
    }

    public static <T> OneBasedPage<T> of(Page<T> page) {
        return new OneBasedPage<>(page);
    }
}
