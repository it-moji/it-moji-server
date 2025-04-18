package com.itmoji.itmojiserver.api.v1.attendance.service;


import com.itmoji.itmojiserver.api.v1.attendance.AttendanceDetailOption;
import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOption;
import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOptions;
import com.itmoji.itmojiserver.api.v1.attendance.Badge;
import com.itmoji.itmojiserver.api.v1.attendance.DetailOption;
import com.itmoji.itmojiserver.api.v1.attendance.dto.AttendanceCategoryDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.DetailOptionDTO;
import com.itmoji.itmojiserver.api.v1.attendance.repository.AttendanceDetailOptionRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.AttendanceOptionRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.BadgeRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.DetailOptionRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceOptionRepository attendanceOptionRepository;
    private final DetailOptionRepository detailOptionRepository;
    private final BadgeRepository badgeRepository;
    private final AttendanceDetailOptionRepository attendanceDetailOptionRepository;

    @Transactional(readOnly = true)
    public Map<String, AttendanceCategoryDTO> getAllAttendanceOptions() {
        List<AttendanceOption> options = attendanceOptionRepository.findAllWithDetailOptions();
        Map<String, AttendanceCategoryDTO> result = new HashMap<>();

        for (AttendanceOption option : options) {
            final List<DetailOptionDTO> detailOptions = option.getDetailOptions()
                    .stream()
                    .map(detail -> new DetailOptionDTO(detail.getId(), detail.getName()))
                    .toList();
            AttendanceCategoryDTO dto = new AttendanceCategoryDTO(option.getName(), detailOptions);
            result.put(option.getCode().name().toLowerCase(), dto);

        }
        return result;
    }

    @Transactional
    public void createOption(final String code) {
        final AttendanceOptions attendanceOptions = AttendanceOptions.find(code);

        if (attendanceOptionRepository.existsById(attendanceOptions)) {
            throw new IllegalArgumentException("이미 존재하는 Option입니다: " + code);
        }

        AttendanceOption option = new AttendanceOption(attendanceOptions, attendanceOptions.getDisplayName());
        attendanceOptionRepository.save(option);
    }

    @Transactional
    public void addDetailOption(final AttendanceOptions options, final String name) {

        detailOptionRepository.findByName(name)
                .ifPresent(detailOption -> {
                    throw new IllegalArgumentException("이미 존재하는 이름 입니다.");
                });

        final AttendanceOption option = attendanceOptionRepository.findByOptions(options)
                .orElseThrow(() -> new IllegalArgumentException("해당 출석 옵션을 존재하지 않아요"));

        // 2) DetailOption 생성
        DetailOption newDetailOption = new DetailOption(name);

        // 3) 외래키 연관관계 설정
        newDetailOption.setAttendanceOption(option);

        // 4) 저장
        detailOptionRepository.save(newDetailOption);
    }

    @Transactional
    public List<DetailOptionDTO> getDetailOption(final AttendanceOptions options) {

        final AttendanceOption option = attendanceOptionRepository.findByOptions(options)
                .orElseThrow(() -> new IllegalArgumentException("해당 출석 옵션을 존재하지 않습니다."));
        final List<DetailOption> detailOptions = detailOptionRepository.findByOption(option);

        return detailOptions.stream()
                .map(detailOption -> new DetailOptionDTO(detailOption.getId(), detailOption.getName()))
                .toList();
    }

    @Transactional
    public void deleteDetailOption(final AttendanceOptions options, final Long detailOptionId) {
        final AttendanceOption option = attendanceOptionRepository.findByOptions(options)
                .orElseThrow(() -> new IllegalArgumentException("해당 출석 옵션을 존재하지 않습니다."));

        DetailOption detailOption = detailOptionRepository.findById(detailOptionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상세 옵션입니다."));

        final AttendanceDetailOption attendanceDetailOption = attendanceDetailOptionRepository.findById(detailOptionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상세 옵션이에요"));

        if (detailOption.getAttendanceOption() != option) {
            throw new IllegalStateException("해당 옵션에 속하지 않은 상세 옵션입니다.");
        }

        detailOptionRepository.delete(detailOption);
        attendanceOptionRepository.delete(option);
        attendanceDetailOptionRepository.delete(attendanceDetailOption);
    }

    @Transactional(readOnly = true)
    public List<Badge> getBadges() {
        return badgeRepository.findAll();
    }

    public void createBadge(final Badge badge) {
        badgeRepository.save(badge);
    }
}
