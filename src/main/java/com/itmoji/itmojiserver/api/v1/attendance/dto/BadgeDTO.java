package com.itmoji.itmojiserver.api.v1.attendance.dto;

public class BadgeDTO {
    private Long id;
    private String icon;
    private String name;

    public BadgeDTO(Long id, String icon, String name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public String getIcon() {
        return icon;
    }
    public String getName() {
        return name;
    }
}
