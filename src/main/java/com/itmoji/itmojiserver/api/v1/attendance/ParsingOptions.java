package com.itmoji.itmojiserver.api.v1.attendance;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class ParsingOptions {
    @Id
    private Long id; // 항상 1번

    @Embedded
    private Delimiter delimiter;

    @Embedded
    private DayMapping dayMapping;

    private String name;

    public ParsingOptions() {
    }

    public ParsingOptions(Long id, Delimiter delimiter, DayMapping dayMapping, String name) {
        this.id = id;
        this.delimiter = delimiter;
        this.dayMapping = dayMapping;
        this.name = name;
    }

    public void updateDelimiter(final Delimiter delimiter) {
        this.delimiter = delimiter;
    }

    public void updateDayMapping(final DayMapping dayMapping) {
        this.dayMapping = dayMapping;
    }

    public void updateName(final String name) {
        this.name = name;
    }
}
