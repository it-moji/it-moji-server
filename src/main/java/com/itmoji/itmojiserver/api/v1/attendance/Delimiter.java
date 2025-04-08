package com.itmoji.itmojiserver.api.v1.attendance;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Delimiter {
    private String person;
    private String line;
    private String title;

    public Delimiter() {
    }

    public Delimiter(String person, String line, String title) {
        this.person = person;
        this.line = line;
        this.title = title;
    }
}
