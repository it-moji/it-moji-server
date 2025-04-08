package com.itmoji.itmojiserver.api.v1.attendance;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class AttendanceDetailOption {
    @Id
    private Long id;

    private Long parsingOptionsId;

    private String name;
    private String identifier;

    public AttendanceDetailOption() {
    }

    public AttendanceDetailOption(Long id, Long parsingOptionsId, String name, String identifier) {
        this.id = id;
        this.parsingOptionsId = parsingOptionsId;
        this.name = name;
        this.identifier = identifier;
    }
}
