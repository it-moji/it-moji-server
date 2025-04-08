package com.itmoji.itmojiserver.api.v1.attendance.repository;

import com.itmoji.itmojiserver.api.v1.attendance.BadgeCondition;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeConditionRepository extends JpaRepository<BadgeCondition, Long> {
    List<BadgeCondition> findByBadgeConditionGroupId(Long groupId);
}
