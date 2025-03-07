package com.meossamos.smore.domain.study.schedule.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.domain.study.schedule.dto.AddStudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.dto.UpdateStudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.schedule.repository.StudyScheduleRepository;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.domain.study.studyMember.repository.StudyMemberRepository;
import com.meossamos.smore.global.exception.MemberNotFoundException;
import com.meossamos.smore.global.exception.StudyNotFoundException;
import com.meossamos.smore.global.exception.StudyScheduleNotFoundException;
import com.meossamos.smore.global.jwt.TokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyScheduleService {
    
    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;


    // 테스트용 일정 저장
    public StudySchedule saveStudySchedule(String title, String content, LocalDateTime startDate, LocalDateTime endDate, Boolean allDay, Member member, Study study) {
        StudySchedule studySchedule = StudySchedule.builder()
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .allDay(allDay)
                .member(member)
                .study(study)
                .build();

        return studyScheduleRepository.save(studySchedule);
    }


    // 일정 생성
    // TODO : 작성자 가져와서 member에 집어넣기
    public StudySchedule addStudySchedule(Long study_id, AddStudyScheduleDto addstudyScheduleDto) {

        Study study = studyRepository.findById(study_id).orElseThrow(() ->
                new StudyNotFoundException("Study not found with ID: " + study_id)
        );

        Member member = memberRepository.findById(1L).orElseThrow(() ->
                new MemberNotFoundException("Member not found with ID: " + "member_Id")
        );


        StudySchedule studySchedule = StudySchedule.builder()
                .title(addstudyScheduleDto.getTitle())
                .content(addstudyScheduleDto.getContent())
                .startDate(addstudyScheduleDto.getStartDate())
                .endDate(addstudyScheduleDto.getEndDate())
                .allDay(addstudyScheduleDto.isAllDay())
                .member(member)
                .study(study)
                .build();

        if (studySchedule == null) {
            throw new RuntimeException("Schedule creation failed.");
        }
        System.out.println(studySchedule.toString());
        return studyScheduleRepository.save(studySchedule);
    }

    // 삭제
    public void deleteSchedule(Long eventId) {

        StudySchedule studySchedule = studyScheduleRepository.findById(eventId).orElseThrow(() ->
                new StudyScheduleNotFoundException("StudySchedule not found with ID " + eventId));
        studyScheduleRepository.delete(studySchedule);

    }

    // 수정
    @Transactional
    public void updateSchedule(UpdateStudyScheduleDto updateStudyScheduleDto) {
        Long target_id = updateStudyScheduleDto.getId();
        StudySchedule target_schedule = studyScheduleRepository.findById(target_id).orElseThrow(() ->
                new StudyScheduleNotFoundException("StudySchedule not found with ID " + target_id));

        target_schedule.setTitle(updateStudyScheduleDto.getTitle());
        target_schedule.setContent(updateStudyScheduleDto.getContent());
        target_schedule.setStartDate(updateStudyScheduleDto.getStartDate());
        target_schedule.setEndDate(updateStudyScheduleDto.getEndDate());


        studyScheduleRepository.save(target_schedule);
    }

    // 이용자 권한 확인
    public boolean checkManager(String accessToken, Long study_id) {
        Authentication authentication =tokenProvider.getAuthentication(accessToken);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Long userID = Long.valueOf(Integer.parseInt(principal.getUsername()));

        Study targetStudy = studyRepository.findById(study_id)
                .orElseThrow(()-> new RuntimeException("해당 ID의 스터디를 찾을 수 없습니다." + study_id));


        Member targetMember = memberRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("해당 ID의 회원을 찾을 수 없습니다." + userID));

        StudyMember studyMember = studyMemberRepository.findByMemberAndStudy(targetMember, targetStudy)
                .orElseThrow(()->new RuntimeException("해당 ID의 스터디회원을 찾을 수 없습니다." + study_id + userID));


//        System.out.println(studyMember.toString());
        System.out.println(studyMember.getPermissionCalendarManage());
        boolean permission = studyMember.getPermissionCalendarManage();
        return permission;
    }
}
