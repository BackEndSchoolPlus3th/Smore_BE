package com.meossamos.smore.domain.study.studyMember.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.domain.study.studyMember.repository.StudyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyMemberService {
    private final StudyMemberRepository studyMemberRepository;

    public StudyMember saveStudyMember(Member member, Study study, Boolean permissionRecruitManage, Boolean permissionArticleManage, Boolean permissionCalendarManage, Boolean permissionSettingManage) {
        StudyMember studyMember = StudyMember.builder()
                .member(member)
                .study(study)
                .permissionRecruitManage(permissionRecruitManage)
                .permissionArticleManage(permissionArticleManage)
                .permissionCalendarManage(permissionCalendarManage)
                .permissionSettingManage(permissionSettingManage)
                .build();

        return studyMemberRepository.save(studyMember);
    }
}
