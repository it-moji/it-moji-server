package com.itmoji.itmojiserver.api.v1.attendance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String icon;
    private String name;

    public Badge(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public void updateIcon(String icon) {
        this.icon = icon;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
