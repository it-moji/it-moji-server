package com.itmoji.itmojiserver.api.v1.attendance.repository;

import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOption;
import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOptions;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceOptionRepository extends JpaRepository<AttendanceOption, AttendanceOptions> {


    @Query("SELECT DISTINCT ao FROM AttendanceOption ao LEFT JOIN FETCH ao.detailOptions")
    List<AttendanceOption> findAllWithDetailOptions();

    @Query("SELECT ao FROM AttendanceOption ao WHERE ao.code = :attendanceOptions")
    Optional<AttendanceOption> findByOptions(AttendanceOptions attendanceOptions);

    Optional<AttendanceOption> findByName(String code);
}
