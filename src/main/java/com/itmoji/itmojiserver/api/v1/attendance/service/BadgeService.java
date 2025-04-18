package com.itmoji.itmojiserver.api.v1.attendance.service;

import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOptions;
import com.itmoji.itmojiserver.api.v1.attendance.Badge;
import com.itmoji.itmojiserver.api.v1.attendance.BadgeCondition;
import com.itmoji.itmojiserver.api.v1.attendance.BadgeConditionGroup;
import com.itmoji.itmojiserver.api.v1.attendance.dto.BadgeConditionDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.BadgeConditionRequest;
import com.itmoji.itmojiserver.api.v1.attendance.dto.BadgeDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.BadgeRequest;
import com.itmoji.itmojiserver.api.v1.attendance.dto.BadgeUpdateDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.BadgeWithConditionsDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.ConditionCreateRequest;
import com.itmoji.itmojiserver.api.v1.attendance.dto.ConditionDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.ConditionGroupDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.ConditionUpdateRequest;
import com.itmoji.itmojiserver.api.v1.attendance.repository.AttendanceOptionRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.BadgeConditionGroupRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.BadgeConditionRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.BadgeRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.DetailOptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final BadgeConditionGroupRepository groupRepository;
    private final BadgeConditionRepository conditionRepository;
    private final DetailOptionRepository detailOptionRepository;
    private final AttendanceOptionRepository attendanceOptionRepository;

    @Transactional(readOnly = true)
    public List<BadgeDTO> getBadges() {
        return badgeRepository.findAll().stream()
                .map(badge -> new BadgeDTO(badge.getId(), badge.getIcon(), badge.getName()))
                .toList();
    }


    @Transactional(readOnly = true)
    public List<BadgeWithConditionsDTO> getBadgesWithConditions() {
        List<Badge> badges = badgeRepository.findAll();
        return badges.stream().map(badge -> {
            List<BadgeConditionGroup> groups = groupRepository.findByBadgeId(badge.getId());
            List<ConditionGroupDTO> groupDTOs = groups.stream().map(group -> {
                List<BadgeCondition> conditions = conditionRepository.findByBadgeConditionGroupId(group.getId());

                List<ConditionDTO> conditionDTOs = conditions.stream().map(condition ->
                        new ConditionDTO(condition.getId(),
                                condition.getConditionKey().name().toLowerCase(),
                                condition.getDetailKeyId(),
                                condition.getCount(),
                                condition.getConditionRange().name().toLowerCase())).toList();

                return new ConditionGroupDTO(group.getId(), conditionDTOs);
            }).toList();
            return new BadgeWithConditionsDTO(badge.getId(), badge.getIcon(), badge.getName(), groupDTOs);
        }).toList();
    }

    @Transactional
    public void createCondition(Long badgeId, ConditionCreateRequest request) {
        badgeRepository.findById(badgeId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Badge not found with id: " + badgeId));

        BadgeConditionGroup group = groupRepository.findById(request.getBadgeConditionGroupId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "BadgeConditionGroup not found with id: " + request.getBadgeConditionGroupId()));
        if (!group.getBadgeId().equals(badgeId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The condition group does not belong to the specified badge.");
        }

        BadgeCondition.Range rangeEnum = parseRange(request.getRange());
        BadgeCondition condition = new BadgeCondition(
                request.getBadgeConditionGroupId(),
                request.getKey(),
                request.getDetailKeyId(),
                request.getCount(),
                rangeEnum
        );

        conditionRepository.save(condition);
    }


    @Transactional
    public void createBadgeWithConditions(BadgeRequest request) {
        Badge badge = new Badge(request.getIcon(), request.getName());
        badge = badgeRepository.save(badge);

        for (List<BadgeConditionRequest> groupOptions : request.getConditionGroups()) {
            BadgeConditionGroup group = new BadgeConditionGroup(badge.getId());
            group = groupRepository.save(group);

            for (BadgeConditionRequest conditionReq : groupOptions) {
                BadgeCondition.Range rangeEnum = parseRange(conditionReq.range());

                BadgeCondition condition = new BadgeCondition(
                        group.getId(),
                        AttendanceOptions.valueOf(conditionReq.key().toUpperCase()),
                        conditionReq.detailKeyId(),
                        conditionReq.count(),
                        rangeEnum
                );
                conditionRepository.save(condition);
            }
        }
    }

    @Transactional
    public void updateCondition(Long badgeId, Long conditionId, ConditionUpdateRequest request) {
        badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 배지 입니다." + badgeId));
        BadgeCondition condition = conditionRepository.findById(conditionId)
                .orElseThrow(() -> new IllegalArgumentException("배지 조건이 존재하지 않습니다. " + conditionId));
        BadgeConditionGroup group = groupRepository.findById(condition.getBadgeConditionGroupId())
                .orElseThrow(() -> new IllegalArgumentException("배지 조건 그룹이 존재하지 않습니다."));

        if (!group.getBadgeId().equals(badgeId)) {
            throw new IllegalArgumentException("해당 컨디션이 배지에 속하지 않습니다.");
        }
        BadgeCondition.Range rangeEnum = parseRange(request.range());

        condition.update(request.key(), request.detailKeyId(), request.count(),
                rangeEnum);
        conditionRepository.save(condition);
    }

    @Transactional
    public void updateBadgeWithAll(Long badgeId, BadgeUpdateDTO dto) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "해당 배지를 찾을 수 없습니다. badgeId=" + badgeId));
        badge.updateIcon(dto.getIcon());
        badge.updateName(dto.getName());
        badgeRepository.save(badge);

        List<BadgeConditionGroup> oldGroups = groupRepository.findByBadgeId(badgeId);
        for (BadgeConditionGroup group : oldGroups) {
            List<BadgeCondition> oldConditions = conditionRepository.findByBadgeConditionGroupId(group.getId());
            conditionRepository.deleteAll(oldConditions);
        }
        groupRepository.deleteAll(oldGroups);

        if (dto.getConditionGroups() == null) {
            return;
        }

        for (List<BadgeConditionDTO> conditionsList : dto.getConditionGroups()) {
            // 새 조건 그룹 생성 (ID는 DB에서 부여됨)
            BadgeConditionGroup newGroup = BadgeConditionGroup.builder()
                    .badgeId(badgeId)
                    .build();
            newGroup = groupRepository.save(newGroup);

            // 그룹에 속한 조건들을 생성
            if (conditionsList != null) {
                for (BadgeConditionDTO condDTO : conditionsList) {
                    BadgeCondition.Range rangeEnum = parseRange(condDTO.getRange());
                    detailOptionRepository.findById(condDTO.getDetailKeyId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "존재하지 않는 detailKeyId 입니다 :" + condDTO.getDetailKeyId()));
                    BadgeCondition newCondition = BadgeCondition.builder()
                            .badgeConditionGroupId(newGroup.getId())
                            .key(condDTO.getKey())
                            .detailKeyId(condDTO.getDetailKeyId())
                            .count(condDTO.getCount())
                            .range(rangeEnum)
                            .build();
                    conditionRepository.save(newCondition);
                }
            }
        }
    }

    private BadgeCondition.Range parseRange(String range) {
        if ("MORE".equalsIgnoreCase(range)) {
            return BadgeCondition.Range.MORE;
        } else if ("LESS".equalsIgnoreCase(range)) {
            return BadgeCondition.Range.LESS;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 range 값입니다: " + range);
        }
    }


    @Transactional
    public void deleteCondition(Long badgeId, Long conditionId) {
        badgeRepository.findById(badgeId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Badge not found with id: " + badgeId));

        BadgeCondition condition = conditionRepository.findById(conditionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "BadgeCondition not found with id: " + conditionId));

        BadgeConditionGroup group = groupRepository.findById(condition.getBadgeConditionGroupId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "BadgeConditionGroup not found for condition."));
        if (!group.getBadgeId().equals(badgeId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The condition does not belong to the specified badge.");
        }

        conditionRepository.deleteById(conditionId);
    }

    @Transactional(readOnly = true)
    public BadgeWithConditionsDTO getBadgeWithConditions(Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 배지를 찾을 수 없습니다."));

        List<BadgeConditionGroup> groups = groupRepository.findByBadgeId(badge.getId());

        List<ConditionGroupDTO> groupDTOs = groups.stream()
                .map(group -> {
                    List<BadgeCondition> conditions = conditionRepository.findByBadgeConditionGroupId(group.getId());
                    List<ConditionDTO> conditionDTOs = conditions.stream()
                            .map(condition -> new ConditionDTO(
                                    condition.getId(),
                                    condition.getConditionKey().name().toLowerCase(),
                                    condition.getDetailKeyId(),
                                    condition.getCount(),
                                    condition.getConditionRange().name().toLowerCase()
                            ))
                            .toList();
                    return new ConditionGroupDTO(group.getId(), conditionDTOs);
                })
                .toList();

        return new BadgeWithConditionsDTO(badge.getId(), badge.getIcon(), badge.getName(), groupDTOs);
    }

    public void deleteBadge(final Long badgeId) {
        final Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배지입니다."));

        badgeRepository.delete(badge);
        List<BadgeConditionGroup> groups = groupRepository.findByBadgeId(badge.getId());
        groups.forEach(group -> {
            List<BadgeCondition> conditions = conditionRepository.findByBadgeConditionGroupId(group.getId());
            conditionRepository.deleteAll(conditions);
        });
        groupRepository.deleteAll(groups);
    }
}
