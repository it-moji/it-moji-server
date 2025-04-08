package com.itmoji.itmojiserver.api.v1.attendance.repository;

import com.itmoji.itmojiserver.api.v1.attendance.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

}
