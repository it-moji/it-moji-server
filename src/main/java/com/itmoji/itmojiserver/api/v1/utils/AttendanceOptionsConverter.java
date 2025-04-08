package com.itmoji.itmojiserver.api.v1.utils;

import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOptions;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AttendanceOptionsConverter implements Converter<String, AttendanceOptions> {

    @Override
    public AttendanceOptions convert(String source) {
        try {
            return AttendanceOptions.find(source);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("올바르지 않은 출석 옵션입니다.: " + source, e);
        }
    }
}
