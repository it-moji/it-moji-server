package com.itmoji.itmojiserver.api.v1.attendance.repository;

import com.itmoji.itmojiserver.api.v1.attendance.ParsingOptions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParsingOptionsRepository extends JpaRepository<ParsingOptions, Long> {
}
