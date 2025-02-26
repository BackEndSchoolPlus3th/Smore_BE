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
    private final StudyHashTagService studyHashTagService;
    private final MemberHashTagService memberHashTagService;
    private final StudyArticleService studyArticleService;
    private final StudyDocumentService studyDocumentService;
    private final AlarmService alarmService;
    private final StudyMemberService studyMemberService;
    private final ChatMessageService chatMessageService;
    private final RecruitmentArticleHashTagService recruitmentArticleHashTagService;


    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
        Long maxMemberId = memberService.getMaxMemberId();
        long nextId = (maxMemberId != null ? maxMemberId : 0L) + 1;
        Member member1 = saveMember("testEmail" + nextId + "@test.com", "testPassword" + nextId, "testNickname" + nextId, LocalDate.now(), "testRegion" + nextId);
        nextId++;
        Member member2 = saveMember("testEmail" + nextId + "@test.com", "testPassword" + nextId, "testNickname" + nextId, LocalDate.now(), "testRegion" + nextId);
        Study study = saveStudy("testTitle" + nextId, 1, "testImageUrls" + nextId, "testIntroduction" + nextId, member1);
        GroupChatRoom groupChatRoom = saveGroupChatRoom(study);
        ChatRoom chatRoom = saveChatRoom(member1, member2);
        RecruitmentArticle recruitmentArticle = saveRecruitmentArticle("testTitle" + nextId, "testContent" + nextId, "testRegion" + nextId, "testImageUrls" + nextId, LocalDateTime.now(), LocalDateTime.now(), true, 1, member1, study);
        StudyArticle studyArticle = saveStudyArticle("testTitle" + nextId, "testContent" + nextId, "testImageUrls" + nextId, "testAttachments" + nextId, member1, study);
        StudySchedule studySchedule = saveStudySchedule("testTitle" + nextId, "testContent" + nextId, LocalDateTime.now(), LocalDateTime.now(), member1, study);
        StudyDocument studyDocument = saveStudyDocument("testName" + nextId, "testUrl" + nextId, "testType" + nextId, study, studyArticle);
        Alarm alarm = saveAlarm(member1, "testMessage" + nextId, member2.getId());
        StudyMember studyMember = saveStudyMember(member1, study, true, true, true, true);
        StudyMember studyMember2 = saveStudyMember(member2, study, false, false, false, false);

        List<String> randomHashTags1 = HashTagUtil.getRandomHashTags();
        List<String> randomHashTags2 = HashTagUtil.getRandomHashTags();
        List<String> randomHashTags3 = HashTagUtil.getRandomHashTags();
        List<String> randomHashTags4 = HashTagUtil.getRandomHashTags();

        randomHashTags1.forEach(hashTag -> saveMemberHashTag(hashTag, member1));
        randomHashTags2.forEach(hashTag -> saveMemberHashTag(hashTag, member2));
        randomHashTags3.forEach(hashTag -> saveStudyHashTag(hashTag, study));
        randomHashTags4.forEach(hashTag -> saveRecruitmentArticleHashTag(hashTag, recruitmentArticle));

        for (int i = 0; i < 100; i++) {
            if (i % 4 == 0) {
                saveChatMessage(groupChatRoom.getId().toString(), member1.getId().toString() , "testMessage" + (nextId + i), "testAttachment" + (nextId + i));
            } else if (i % 4 == 1) {
                saveChatMessage(groupChatRoom.getId().toString(), member2.getId().toString() , "testMessage" + (nextId + i), "testAttachment" + (nextId + i));
            } else if (i % 4 == 2) {
                saveChatMessage(chatRoom.getId().toString(), member1.getId().toString() , "testMessage" + (nextId + i), "testAttachment" + (nextId + i));
            } else {
                saveChatMessage(chatRoom.getId().toString(), member2.getId().toString() , "testMessage" + (nextId + i), "testAttachment" + (nextId + i));
            }
        }

        System.out.println("BaseInitDataDev");
        System.out.println("member1: " + member1);
        System.out.println("member2: " + member2);
        System.out.println("study: " + study);
        System.out.println("groupChatRoom: " + groupChatRoom);
        System.out.println("chatRoom: " + chatRoom);
        System.out.println("recruitmentArticle: " + recruitmentArticle);
        System.out.println("studyArticle: " + studyArticle);
        System.out.println("studySchedule: " + studySchedule);
        System.out.println("studyDocument: " + studyDocument);
        System.out.println("alarm: " + alarm);
        System.out.println("studyMember: " + studyMember);
        System.out.println("studyMember2: " + studyMember2);
        };
    }

    private Member saveMember(String email, String password, String nickname, @Nullable LocalDate birthdate, @Nullable String region) {
        return memberService.saveMember(email, password, nickname, birthdate, region);
    }

    private Study saveStudy(String title, Integer memberCnt, @Nullable String imageUrls, @Nullable String introduction, Member leader) {
        return studyService.saveStudy(title, memberCnt, imageUrls, introduction, leader);
    }

    private ChatRoom saveChatRoom(Member member1, Member member2) {
        return chatRoomService.saveChatRoom(member1, member2);
    }

    private RecruitmentArticle saveRecruitmentArticle(String title, String content, @Nullable String region, @Nullable String imageUrls, LocalDateTime startDate, LocalDateTime endDate, Boolean isRecruiting, Integer maxMember, Member member, Study study) {
        return recruitmentArticleService.saveRecruitmentArticle(title, content, region, imageUrls, startDate, endDate, isRecruiting, maxMember, member, study);
    }

    private MemberHashTag saveMemberHashTag(String hashTag, Member member) {
        return memberHashTagService.saveMemberHashTag(hashTag, member);
    }

    private StudyHashTag saveStudyHashTag(String hashTag, Study study) {
        return studyHashTagService.saveStudyHashTag(hashTag, study);
    }

    private RecruitmentArticleHashTag saveRecruitmentArticleHashTag(String hashTag, RecruitmentArticle recruitmentArticle) {
        return recruitmentArticleHashTagService.saveRecruitmentArticleHashTag(hashTag, recruitmentArticle);
    }

    private StudyArticle saveStudyArticle(String title, String content, @Nullable String imageUrls, @Nullable String attachments, Member member, Study study) {
        return studyArticleService.saveStudyArticle(title, content, imageUrls, attachments, member, study);
    }

    private StudySchedule saveStudySchedule(String title, String content,LocalDateTime startDate, LocalDateTime endDate, Member member, Study study) {
        return studyScheduleService.saveStudySchedule(title, content, startDate, endDate, member, study);
    }

    private StudyDocument saveStudyDocument(String name, String url, String type, Study study, StudyArticle studyArticle) {
        return studyDocumentService.saveStudyDocument(name, url, type, study, studyArticle);
    }

    private Alarm saveAlarm(Member member, String message, @Nullable Long senderId) {
        return alarmService.saveAlarm(member, message, senderId);
    }

    private StudyMember saveStudyMember(Member member, Study study, Boolean permissionRecruitManage, Boolean permissionArticleManage, Boolean permissionCalendarManage, Boolean permissionSettingManage) {
        return studyMemberService.saveStudyMember(member, study, permissionRecruitManage, permissionArticleManage, permissionCalendarManage, permissionSettingManage);
    }

    private ChatMessage saveChatMessage(String roomId, String senderId, String message, String attachment) {
        return chatMessageService.saveChatMessage(roomId, senderId, message, attachment);
    }

    private GroupChatRoom saveGroupChatRoom(Study study) {
        return groupChatRoomService.saveGroupChatRoom(study);
    }
}
