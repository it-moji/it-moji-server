package com.itmoji.itmojiserver.api.v1.attendance.repository;

import com.itmoji.itmojiserver.api.v1.attendance.BadgeConditionGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeConditionGroupRepository extends JpaRepository<BadgeConditionGroup, Long> {
    List<BadgeConditionGroup> findByBadgeId(Long badgeId);
}
