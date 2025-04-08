package com.itmoji.itmojiserver.api.v1.attendance.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ParsingOptionsDTO {
    private DelimiterDTO delimiter;
    private DayMappingDTO dayMapping;
    private String name;
    private List<AttendanceDetailOptionDTO> attendanceDetailOptions;

    public ParsingOptionsDTO(DelimiterDTO delimiter, DayMappingDTO dayMapping, String name,
                             List<AttendanceDetailOptionDTO> attendanceDetailOptions) {
        this.delimiter = delimiter;
        this.dayMapping = dayMapping;
        this.name = name;
        this.attendanceDetailOptions = attendanceDetailOptions;
    }
}
