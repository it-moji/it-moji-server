package com.itmoji.itmojiserver.api.v1.attendance.dto;

import lombok.Getter;

@Getter
public class DayMappingDTO {
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;

    public DayMappingDTO() {
    }

    public DayMappingDTO(String monday, String tuesday, String wednesday, String thursday, String friday,
                         String saturday, String sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }
}
