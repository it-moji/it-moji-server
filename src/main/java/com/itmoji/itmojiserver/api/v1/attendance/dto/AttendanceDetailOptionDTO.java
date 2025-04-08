package com.itmoji.itmojiserver.api.v1.attendance.dto;

import lombok.Getter;

@Getter
public class AttendanceDetailOptionDTO {
    private Long id;
    private String name;
    private String identifier;

    public AttendanceDetailOptionDTO() {
    }

    public AttendanceDetailOptionDTO(Long id, String name, String identifier) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
    }
}
