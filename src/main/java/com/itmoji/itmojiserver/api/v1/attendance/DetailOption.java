package com.itmoji.itmojiserver.api.v1.attendance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class DetailOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "attendance_option")
    private AttendanceOption attendanceOption;

    public DetailOption() {
    }

    public DetailOption(final String name) {
        this.name = name;
    }

    public void setAttendanceOption(AttendanceOption attendanceOption) {
        this.attendanceOption = attendanceOption;
    }
}
