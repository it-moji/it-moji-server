package com.itmoji.itmojiserver.api.v1.announcement.dto.request;

public record PostSearchRequestDTO(
        String query,
        String type,
        Integer page,
        Integer size
) {
    private static final int MIN_PAGE = 1;
    private static final int REVISION_PAGE = 1;
    private static final int START_PAGE = 0;
    private static final int DEFAULT_SIZE = 3;

    /**
     * 입력된 페이지 번호가 null이거나 1 미만인 경우 1페이지를 반환합니다.
     * 그렇지 않은 경우는 페이지 -1을 합니다.
     * why? 프론트에서는 첫 페이지 기준이 1이라고 생각하고 모든 로직을 구성합니다.
     */
    public PostSearchRequestDTO {
        page = (page == null || page < MIN_PAGE) ? START_PAGE : page - REVISION_PAGE;
        size = (size == null || size <= 0) ? DEFAULT_SIZE : size;
    }
}
