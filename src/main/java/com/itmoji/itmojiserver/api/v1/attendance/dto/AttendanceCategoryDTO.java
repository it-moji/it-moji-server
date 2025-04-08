package com.itmoji.itmojiserver.api.v1.attendance.dto;

import java.util.List;

public record AttendanceCategoryDTO(String name, List<DetailOptionDTO> detailOptions) {
}
