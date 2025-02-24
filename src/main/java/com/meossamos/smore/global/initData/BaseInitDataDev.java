package com.meossamos.smore.global.initData;

import com.meossamos.smore.domain.main.main.entity.Main;
import com.meossamos.smore.domain.main.main.service.MainService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.domain.study.chat.entity.StudyChat;
import com.meossamos.smore.domain.study.chat.service.StudyChatService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Date;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class BaseInitDataDev {
    private final MemberService memberService;
    private final MainService mainService;
//    private final ArticleService articleService;
    private final StudyChatService studyChatService;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            IntStream.rangeClosed(1, 10).forEach(i -> {
                Member member = saveMember("test" + i + "@test.com", "test password" + i, "test nickname" + i, new Date(), "서울", "test hashTags" + i);
                Main article = saveArticle("test title" + i, "test content" + i, "서울", "test hashTags" + i, "test imageUrls" + i, "test attachments" + i, member);
                System.out.println(article);
                StudyChat studyChat = saveStudyChat("test studyId" + i, "test userId" + i, "test message" + i);
                System.out.println(studyChat);
            });
        };
    }

    private Member saveMember(String email, String password, String nickname, @Nullable Date birthDate, @Nullable String region, @Nullable String hashTags) {
        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .birthdate(birthDate)
                .region(region)
                .hashTags(hashTags)
                .build();

       return memberService.saveMember(member);
    }

    private Main saveArticle(String title, String content, String region, String hashTags, String imageUrls, String attachments, Member member) {
        Main main = Main.builder()
                .title(title)
                .content(content)
                .region(region)
                .hashTags(hashTags)
                .imageUrls(imageUrls)
                .attachments(attachments)
                .member(member)
                .build();

        return mainService.saveArticle(main);
    }

    private StudyChat saveStudyChat(String studyId, String userId, String message) {
        StudyChat studyChat = StudyChat.builder()
                .studyId(studyId)
                .userId(userId)
                .message(message)
                .build();

        return studyChatService.saveStudyChat(studyChat);
    }
}
