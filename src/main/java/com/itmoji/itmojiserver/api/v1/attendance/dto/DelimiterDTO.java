package com.itmoji.itmojiserver.api.v1.attendance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelimiterDTO {
    private String person;
    private String line;
    private String title;

    public DelimiterDTO(String person, String line, String title) {
        this.person = person;
        this.line = line;
        this.title = title;
    }
}
