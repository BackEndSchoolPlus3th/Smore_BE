package com.meossamos.smore;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.global.util.HashTagUtil;
import com.meossamos.smore.global.util.RandomUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddDataTest {
    @Autowired
    private RecruitmentArticleService recruitmentArticleService;
    @Autowired
    private MemberService memberService;
    private final Random random = new Random();
    @Autowired
    private StudyService studyService;


    @Test
    public void addTest() {
        for (int i = 0; i < 1000; i++) {
            addData();
        }
        }

    private void addData() {
        String title = RandomUtil.getRandomTitle();
        String content = RandomUtil.getRandomContent();
        String introduction = RandomUtil.getRandomIntroduction();
        String region = RandomUtil.getRandomRegion();
        int ClipCount = random.nextInt(200);
        int randomImageNum = random.nextInt(5) + 1;
        int maxMember = random.nextInt(50) + 1;
        int currentMember = random.nextInt(maxMember) + 1;
        StringBuilder imageUrls = new StringBuilder();
        LocalDateTime endDate = RandomUtil.getRandomEndDate();


        for (int i = 0; i < randomImageNum; i++) {
            imageUrls.append("https://picsum.photos/400/600?random=1");
            // 마지막 URL이 아니라면 콤마를 추가
            if (i < randomImageNum - 1) {
                imageUrls.append(",");
            }
        }

        List<String> hashTags = HashTagUtil.getRandomHashTags();
        String hashTag = HashTagUtil.mergeHashTagList(hashTags);

        String memberName = RandomUtil.getRandomMemberName();
        String memberEmail = RandomUtil.getRandomEmail();
        String memberPassword = RandomUtil.getRandomPassword(random.nextInt(10) + 3);
        LocalDate memberBirthDate = RandomUtil.getRandomBirthday();
        String memberRigion = RandomUtil.getRandomRegion();
        List<String> memberHashTags = HashTagUtil.getRandomHashTags();
        String memberHashTag = HashTagUtil.mergeHashTagList(memberHashTags);

        Member savedMember = memberService.saveMember(memberPassword + memberEmail, memberPassword, memberName, memberBirthDate, memberRigion, "https://picsum.photos/200/200?random=1");
        Study savedStudy = studyService.saveStudy(title, currentMember, imageUrls.toString(), introduction, savedMember);

        RecruitmentArticle savedRecruitmentArticle = recruitmentArticleService.saveRecruitmentArticle(title, content, introduction, region, imageUrls.toString(), LocalDateTime.now(), endDate, true, maxMember, hashTag, savedMember, savedStudy, ClipCount);

    }
}
