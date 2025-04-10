package com.itmoji.itmojiserver.api.v1.attendance.service;

import com.itmoji.itmojiserver.api.v1.attendance.AttendanceDetailOption;
import com.itmoji.itmojiserver.api.v1.attendance.AttendanceOptions;
import com.itmoji.itmojiserver.api.v1.attendance.DayMapping;
import com.itmoji.itmojiserver.api.v1.attendance.Delimiter;
import com.itmoji.itmojiserver.api.v1.attendance.DetailOption;
import com.itmoji.itmojiserver.api.v1.attendance.ParsingOptions;
import com.itmoji.itmojiserver.api.v1.attendance.dto.AttendanceDetailOptionDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.DayMappingDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.DelimiterDTO;
import com.itmoji.itmojiserver.api.v1.attendance.dto.ParsingOptionsDTO;
import com.itmoji.itmojiserver.api.v1.attendance.repository.AttendanceDetailOptionRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.DetailOptionRepository;
import com.itmoji.itmojiserver.api.v1.attendance.repository.ParsingOptionsRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ParsingOptionService {
    private final ParsingOptionsRepository parsingOptionsRepository;
    private final AttendanceDetailOptionRepository attendanceDetailOptionRepository;
    private final DetailOptionRepository detailOptionRepository;

    @Transactional(readOnly = true)
    public ParsingOptionsDTO getParsingOptions() {
        // 싱글톤으로 관리되는 ParsingOptions (id=1) 조회
        ParsingOptions options = parsingOptionsRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "파싱 옵션을 찾을 수 없습니다."));

        Delimiter delimiter = options.getDelimiter();
        DelimiterDTO delimiterDTO = new DelimiterDTO(delimiter.getPerson(), delimiter.getLine(), delimiter.getTitle());

        DayMapping dayMapping = options.getDayMapping();
        DayMappingDTO dayMappingDTO = new DayMappingDTO(dayMapping.getMonday(),
                dayMapping.getTuesday(),
                dayMapping.getWednesday(),
                dayMapping.getThursday(),
                dayMapping.getFriday(),
                dayMapping.getSaturday(),
                dayMapping.getSunday());

        final List<DetailOption> detailOptions = findAttendanceDetailOptions();

        final List<AttendanceDetailOptionDTO> detailOptionDTOs = detailOptions.stream()
                .map(o -> attendanceDetailOptionRepository.findById(o.getId())
                        .map(option -> new AttendanceDetailOptionDTO(o.getId(), o.getName(), option.getIdentifier()))
                        .orElseGet(() -> new AttendanceDetailOptionDTO(o.getId(), o.getName(), null)))
                .toList();

        return new ParsingOptionsDTO(delimiterDTO, dayMappingDTO, options.getName(), detailOptionDTOs);
    }

    private List<DetailOption> findAttendanceDetailOptions() {
        return detailOptionRepository.findByAttendanceOptions(AttendanceOptions.ATTENDANCE);
    }

    @Transactional
    public void updateParsingOptions(ParsingOptionsDTO optionsDTO) {
        ParsingOptions options = parsingOptionsRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "파싱 옵션을 찾을 수 없습니다."));

        DelimiterDTO delimiterDTO = optionsDTO.getDelimiter();
        options.updateDelimiter(
                new Delimiter(delimiterDTO.getPerson(), delimiterDTO.getLine(), delimiterDTO.getTitle()));

        DayMappingDTO dayMappingDTO = optionsDTO.getDayMapping();
        options.updateDayMapping(
                new DayMapping(dayMappingDTO.getMonday(), dayMappingDTO.getTuesday(), dayMappingDTO.getWednesday(),
                        dayMappingDTO.getThursday(), dayMappingDTO.getFriday(), dayMappingDTO.getSaturday(),
                        dayMappingDTO.getSunday()));

        options.updateName(optionsDTO.getName());

        parsingOptionsRepository.save(options);

        List<AttendanceDetailOption> existingOptions = attendanceDetailOptionRepository.findByParsingOptionsId(1L);
        attendanceDetailOptionRepository.deleteAll(existingOptions);

        if (optionsDTO.getAttendanceDetailOptions() == null) {
            throw new IllegalArgumentException("출석 상세 옵션이 빠져있습니다.");
        }

        final List<DetailOption> detailOptions = findAttendanceDetailOptions();

        List<AttendanceDetailOption> detailOptionsBox = new ArrayList<>();
        for (AttendanceDetailOptionDTO detailDTO : optionsDTO.getAttendanceDetailOptions()) {
            final DetailOption detailOption = detailOptions.stream()
                    .filter(deo -> deo.getName().equals(detailDTO.getName())).findFirst().orElseThrow();
            AttendanceDetailOption attendanceDetailOption = new AttendanceDetailOption(detailOption.getId(), 1L,
                    detailOption.getName(), detailDTO.getIdentifier());
            detailOptionsBox.add(attendanceDetailOption);
        }

        attendanceDetailOptionRepository.saveAll(detailOptionsBox);
    }

    @Transactional
    public void createParsingOptions(ParsingOptionsDTO optionsDTO) {
        if (parsingOptionsRepository.existsById(1L)) {
            throw new IllegalArgumentException("파싱옵션은 하나만 만들 수 있습니다.");
        }

        DelimiterDTO delimiterDTO = optionsDTO.getDelimiter();
        Delimiter delimiter = new Delimiter(delimiterDTO.getPerson(), delimiterDTO.getLine(), delimiterDTO.getTitle());

        DayMappingDTO dayMappingDTO = optionsDTO.getDayMapping();
        DayMapping dayMapping = new DayMapping(dayMappingDTO.getMonday(), dayMappingDTO.getTuesday(),
                dayMappingDTO.getWednesday(),
                dayMappingDTO.getThursday(), dayMappingDTO.getFriday(), dayMappingDTO.getSaturday(),
                dayMappingDTO.getSunday());

        ParsingOptions options = new ParsingOptions(1L, delimiter, dayMapping, optionsDTO.getName());
        parsingOptionsRepository.save(options);

        if (optionsDTO.getAttendanceDetailOptions() == null) {
            throw new IllegalArgumentException("출석 상세 옵션이 포함되어있지 않습니다.");
        }

        final List<DetailOption> detailOptions = findAttendanceDetailOptions();

        List<AttendanceDetailOption> detailOptionsBox = new ArrayList<>();
        for (AttendanceDetailOptionDTO detailDTO : optionsDTO.getAttendanceDetailOptions()) {
            final DetailOption detailOption = detailOptions.stream()
                    .filter(deo -> deo.getName().equals(detailDTO.getName())).findFirst().orElseThrow();
            AttendanceDetailOption attendanceDetailOption = new AttendanceDetailOption(detailOption.getId(), 1L,
                    detailOption.getName(), detailDTO.getIdentifier());
            detailOptionsBox.add(attendanceDetailOption);
        }
        attendanceDetailOptionRepository.saveAll(detailOptionsBox);
    }
}
