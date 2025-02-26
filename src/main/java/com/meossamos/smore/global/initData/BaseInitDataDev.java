package com.meossamos.smore.global.initData;

import com.meossamos.smore.domain.alarm.alarm.entity.Alarm;
import com.meossamos.smore.domain.alarm.alarm.service.AlarmService;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleHashTag.entity.RecruitmentArticleHashTag;
import com.meossamos.smore.domain.article.recruitmentArticleHashTag.service.RecruitmentArticleHashTagService;
import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.article.studyArticle.service.StudyArticleService;
import com.meossamos.smore.domain.chat.chat.entity.ChatRoom;
import com.meossamos.smore.domain.chat.chat.service.ChatRoomService;
import com.meossamos.smore.domain.chat.groupChat.entity.GroupChatRoom;
import com.meossamos.smore.domain.chat.groupChat.service.GroupChatRoomService;
import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import com.meossamos.smore.domain.chat.message.service.ChatMessageService;
import com.meossamos.smore.domain.member.hashTag.entity.MemberHashTag;
import com.meossamos.smore.domain.member.hashTag.service.MemberHashTagService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.domain.study.document.entity.StudyDocument;
import com.meossamos.smore.domain.study.document.service.StudyDocumentService;
import com.meossamos.smore.domain.study.hashTag.entity.StudyHashTag;
import com.meossamos.smore.domain.study.hashTag.service.StudyHashTagService;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.schedule.service.StudyScheduleService;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import com.meossamos.smore.global.util.HashTagUtil;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class BaseInitDataDev {
    private final StudyScheduleService studyScheduleService;
    private final MemberService memberService;
    private final StudyService studyService;
    private final GroupChatRoomService groupChatRoomService;
    private final ChatRoomService chatRoomService;
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
