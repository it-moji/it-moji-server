package com.itmoji.itmojiserver.api.v1.attendance.repository;

import com.itmoji.itmojiserver.api.v1.attendance.AttendanceDetailOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceDetailOptionRepository extends JpaRepository<AttendanceDetailOption, Long> {
    List<AttendanceDetailOption> findByParsingOptionsId(Long parsingOptionsId);

    Optional<AttendanceDetailOption> findById(Long id);

    Optional<AttendanceDetailOption> findByName(String name);
}
