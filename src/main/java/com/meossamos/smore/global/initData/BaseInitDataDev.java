package com.meossamos.smore.global.initData;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class BaseInitDataDev {
    private final StudyScheduleService studyScheduleService;
    private final MemberService memberService;
    private final StudyService studyService;
    private final GroupChatRoomService groupChatRoomService;
    private final DmRoomService chatRoomService;
    private final RecruitmentArticleService recruitmentArticleService;
    private final StudyArticleService studyArticleService;
    private final StudyDocumentService studyDocumentService;
    private final AlarmService alarmService;
    private final StudyMemberService studyMemberService;
    private final ChatMessageService chatMessageService;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
        };
    }

}