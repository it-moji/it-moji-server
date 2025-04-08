package com.itmoji.itmojiserver.api.v1.attendance.service;

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
import com.itmoji.itmojiserver.api.v1.attendance.repository.BadgeConditionGroupRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.BadgeConditionRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.BadgeRepository;
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
                                new ConditionDTO(condition.getId(), condition.getConditionKey(), condition.getCount(),
                                        condition.getConditionRange().name()))
                        .toList();
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

        BadgeCondition.Range rangeEnum;
        if ("more".equalsIgnoreCase(request.getRange())) {
            rangeEnum = BadgeCondition.Range.MORE;
        } else if ("less".equalsIgnoreCase(request.getRange())) {
            rangeEnum = BadgeCondition.Range.LESS;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid range value: " + request.getRange());
        }

        BadgeCondition condition = new BadgeCondition(
                request.getBadgeConditionGroupId(),
                request.getKey(),
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
                BadgeCondition.Range rangeEnum;
                if ("more".equalsIgnoreCase(conditionReq.getRange())) {
                    rangeEnum = BadgeCondition.Range.MORE;
                } else if ("less".equalsIgnoreCase(conditionReq.getRange())) {
                    rangeEnum = BadgeCondition.Range.LESS;
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "유효하지 않은 range 값입니다: " + conditionReq.getRange());
                }

                BadgeCondition condition = new BadgeCondition(
                        group.getId(),
                        conditionReq.getKey(),
                        conditionReq.getCount(),
                        rangeEnum
                );
                conditionRepository.save(condition);
            }
        }
    }

    @Transactional
    public void updateCondition(Long badgeId, Long conditionId, ConditionUpdateRequest request) {
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

        // range 문자열을 enum으로 변환
        BadgeCondition.Range rangeEnum;
        if ("more".equalsIgnoreCase(request.getRange())) {
            rangeEnum = BadgeCondition.Range.MORE;
        } else if ("less".equalsIgnoreCase(request.getRange())) {
            rangeEnum = BadgeCondition.Range.LESS;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid range value: " + request.getRange());
        }

        condition.update(request.getKey(), request.getCount(), rangeEnum);
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
                    BadgeCondition newCondition = BadgeCondition.builder()
                            .badgeConditionGroupId(newGroup.getId())
                            .key(condDTO.getKey())
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
                                    condition.getConditionKey(),
                                    condition.getCount(),
                                    condition.getConditionRange().name()
                            ))
                            .toList();
                    return new ConditionGroupDTO(group.getId(), conditionDTOs);
                })
                .toList();

        return new BadgeWithConditionsDTO(badge.getId(), badge.getIcon(), badge.getName(), groupDTOs);
    }
}
