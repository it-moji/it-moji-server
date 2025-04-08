package com.itmoji.itmojiserver.api.v1.attendance;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class AttendanceOption {

    @Id
    @Enumerated(EnumType.STRING)
    private AttendanceOptions code;

    private String name;

    @OneToMany(mappedBy = "attendanceOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailOption> detailOptions = new ArrayList<>();

    public AttendanceOption() {
    }

    public AttendanceOption(final AttendanceOptions code, final String name) {
        this.code = code;
        this.name = name;
    }
}

