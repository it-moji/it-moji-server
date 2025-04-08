package com.itmoji.itmojiserver.api.v1.attendance;

import java.util.Arrays;

public enum AttendanceOptions {
    ATTENDANCE("출석"),
    ABSENCE("결석"),
    GAP("공결"),
    REST("휴식"),
    VACATION("휴가");

    private final String displayName;

    AttendanceOptions(final String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AttendanceOptions find(final String category) {
        return Arrays.stream(AttendanceOptions.values())
                .filter(attendanceOptions -> attendanceOptions.name().equals(category.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 출석 옵션입니다." + category));
    }
}
