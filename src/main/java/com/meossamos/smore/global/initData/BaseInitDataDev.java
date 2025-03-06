package com.meossamos.smore.global.initData;


import com.meossamos.smore.domain.alarm.alarm.service.AlarmService;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.studyArticle.service.StudyArticleService;
import com.meossamos.smore.domain.chat.dm.service.DmRoomService;
import com.meossamos.smore.domain.chat.groupChat.service.GroupChatRoomService;
import com.meossamos.smore.domain.chat.message.service.ChatMessageService;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.domain.study.document.service.StudyDocumentService;
import com.meossamos.smore.domain.study.schedule.service.StudyScheduleService;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class BaseInitDataDev {

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
        };
    }

}