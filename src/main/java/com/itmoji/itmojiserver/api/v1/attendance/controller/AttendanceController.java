package com.itmoji.itmojiserver.api.v1.attendance.controller;

import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOptions;
import com.itmoji.itmojiserver.api.v1.attendance.dto.AttendanceCategoryDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.BadgeDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.BadgeRequest;
import com.itmoji.itmojiserver.api.v1.attendance.dto.BadgeUpdateDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.BadgeWithConditionsDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.ConditionCreateRequest;
import com.itmoji.itmojiserver.api.v1.attendance.dto.ConditionUpdateRequest;
import com.itmoji.itmojiserver.api.v1.attendance.dto.CreateDetailOptionRequest;
import com.itmoji.itmojiserver.api.v1.attendance.dto.DetailOptionDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.ParsingOptionsDTO;
import com.itmoji.itmojiserver.api.v1.attendance.service.AttendanceService;
import com.itmoji.itmojiserver.api.v1.attendance.service.BadgeService;
import com.itmoji.itmojiserver.api.v1.attendance.service.ParsingOptionService;
import com.itmoji.itmojiserver.api.v1.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance")
@Tag(name = "출석", description = "AttendanceController")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final BadgeService badgeService;
    private final ParsingOptionService parsingOptionService;

    @GetMapping("/options")
    @Operation(summary = "모든 출석 옵션 조회", description = "모든 출석 옵션을 조회하는 로직입니다.")
    public ApiResponse<Map<String, AttendanceCategoryDTO>> geAttendanceOptions() {
        final Map<String, AttendanceCategoryDTO> allAttendanceOptions = attendanceService.getAllAttendanceOptions();
        return ApiResponse.from(HttpStatus.OK, "성공", allAttendanceOptions);
    }

    @PostMapping("/options")
    @Operation(summary = "출석 옵션 추가", description = "출석 옵션을 추가합니다.")
    public ApiResponse<Void> createOption(@RequestParam("category") String code) {
        attendanceService.createOption(code);
        return ApiResponse.from(HttpStatus.CREATED, "성공", null);
    }

    @PostMapping("/options/{option}")
    @Operation(summary = "특정 출석 상세 옵션 추가", description = "특정 출석 상세 옵션을 추가합니다.")
    public ApiResponse<Void> addDetailOption(
            @PathVariable AttendanceOptions option,
            @RequestBody CreateDetailOptionRequest request
    ) {
        attendanceService.addDetailOption(option, request.name());
        return ApiResponse.from(HttpStatus.CREATED, "성공", null);
    }

    @GetMapping("/options/{option}")
    @Operation(summary = "특정 출석 상세 옵션 조회", description = "출석 상세 옵션을 조회합니다.")
    public ApiResponse<List<DetailOptionDTO>> getDetailOption(@PathVariable AttendanceOptions option) {
        final List<DetailOptionDTO> detailOptions = attendanceService.getDetailOption(option);
        return ApiResponse.from(HttpStatus.OK, "성공", detailOptions);
    }

    @DeleteMapping("/options/{option}/{detailOptionId}")
    @Operation(summary = "특정 출석 상세 옵션 제거", description = "출석 상세 옵션을 제합니다.")
    public ApiResponse<Void> deleteDetailOption(
            @PathVariable AttendanceOptions option,
            @PathVariable Long detailOptionId
    ) {
        attendanceService.deleteDetailOption(option, detailOptionId);
        return ApiResponse.from(HttpStatus.OK, "성공", null);
    }

    @GetMapping("/badges")
    @Operation(summary = "배지 목록 조회", description = "모든 배지의 기본 정보를 조회합니다.")
    public ApiResponse<List<BadgeDTO>> getBadges() {
        List<BadgeDTO> badges = badgeService.getBadges();
        return ApiResponse.from(HttpStatus.OK, "성공", badges);
    }

    @GetMapping("/badges/conditions")
    @Operation(summary = "배지와 조건 조회", description = "모든 배지와 해당 배지를 획득할 수 있는 조건들을 함께 조회합니다.")
    public ApiResponse<List<BadgeWithConditionsDTO>> getBadgesWithConditions() {
        List<BadgeWithConditionsDTO> badges = badgeService.getBadgesWithConditions();
        return ApiResponse.from(HttpStatus.OK, "성공", badges);
    }

    @PostMapping("/badges")
    @Operation(summary = "배지 생성", description = "배지와 획득 조건을 함께 생성합니다.")
    public ApiResponse<Void> createBadge(@RequestBody @Valid BadgeRequest request) {
        badgeService.createBadgeWithConditions(request);
        return ApiResponse.from(HttpStatus.CREATED, "배지가 생성되었습니다.", null);
    }

    @PostMapping("/badges/{badgeId}/condition-groups")
    @Operation(summary = "배지 기준 추가", description = "특정 배지의 새로운 배지 조건을 추가합니다.")
    public ApiResponse<Void> createCondition(@PathVariable Long badgeId,
                                             @RequestBody @Valid ConditionCreateRequest request) {
        request.setBadgeId(badgeId);
        badgeService.createCondition(badgeId, request);
        return ApiResponse.from(HttpStatus.CREATED, "배지 조건이 추가되었습니다.", null);
    }

    @PutMapping("/badges/{badgeId}/condition-groups/{conditionGroupId}")
    @Operation(summary = "배지 기준 수정", description = "특정 배지의 조건을 수정합니다.")
    public ApiResponse<Void> updateCondition(@PathVariable Long badgeId, @PathVariable Long conditionGroupId,
                                             @RequestBody @Valid ConditionUpdateRequest request) {
        badgeService.updateCondition(badgeId, conditionGroupId, request);
        return ApiResponse.from(HttpStatus.OK, "배지 조건이 수정되었습니다.", null);
    }

    @DeleteMapping("/badges/{badgeId}/condition-groups/{conditionGroupId}")
    @Operation(summary = "배지 기준 삭제", description = "특정 배지의 조건을 삭제합니다.")
    public ApiResponse<Void> deleteCondition(@PathVariable Long badgeId, @PathVariable Long conditionGroupId) {
        badgeService.deleteCondition(badgeId, conditionGroupId);
        return ApiResponse.from(HttpStatus.OK, "배지 조건이 삭제되었습니다.", null);
    }

    @PutMapping("/badges/{badgeId}")
    @Operation(summary = "배지 전체 업데이트", description = "배지와 해당 조건 그룹 및 조건들을 한 번에 업데이트합니다.")
    public ApiResponse<Void> updateBadgeWithAll(
            @PathVariable Long badgeId,
            @RequestBody @Valid BadgeUpdateDTO dto
    ) {
        badgeService.updateBadgeWithAll(badgeId, dto);
        return ApiResponse.from(HttpStatus.OK, "전체 저장이 완료되었습니다.", null);
    }

    @GetMapping("/parsing-options")
    @Operation(summary = "파싱 옵션 조회", description = "모든 파싱 옵션을 조회합니다.")
    public ApiResponse<ParsingOptionsDTO> getParsingOptions() {
        ParsingOptionsDTO dto = parsingOptionService.getParsingOptions();
        return ApiResponse.from(HttpStatus.OK, "성공", dto);
    }

    @PutMapping("/parsing-options")
    @Operation(summary = "파싱 옵션 수정", description = "파싱 옵션을 수정합니다.")
    public ApiResponse<Void> updateParsingOptions(@RequestBody @Valid ParsingOptionsDTO optionsDTO) {
        parsingOptionService.updateParsingOptions(optionsDTO);
        return ApiResponse.from(HttpStatus.OK, "파싱 옵션이 업데이트되었습니다.", null);
    }

    @PostMapping("/parsing-options")
    @Operation(summary = "파싱 옵션 생성", description = "새 파싱 옵션을 생성합니다.")
    public ApiResponse<Void> createParsingOptions(@RequestBody @Valid ParsingOptionsDTO request) {
        parsingOptionService.createParsingOptions(request);
        return ApiResponse.from(HttpStatus.CREATED, "파싱 옵션이 생성되었습니다.", null);
    }
}
