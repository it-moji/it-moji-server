package com.itmoji.itmojiserver.api.v1.attendance.repository;

import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOption;
import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOptions;
import com.itmoji.itmojiserver.api.v1.attendance.DetailOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DetailOptionRepository extends JpaRepository<DetailOption, Long> {

    @Query("SELECT do FROM DetailOption do WHERE do.attendanceOption = :attendanceOption")
    List<DetailOption> findByOption(AttendanceOption attendanceOption);

    @Query("SELECT do FROM DetailOption do WHERE do.attendanceOption.code = :attendanceOptions")
    List<DetailOption> findByAttendanceOptions(AttendanceOptions attendanceOptions);
}
