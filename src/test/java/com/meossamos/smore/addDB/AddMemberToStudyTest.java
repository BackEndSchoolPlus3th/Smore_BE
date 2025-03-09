package com.meossamos.smore.addDB;

import com.meossamos.smore.domain.study.studyMember.entity.StudyPosition;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddMemberToStudyTest {
    @Autowired
    private StudyMemberService studyMemberService;

    @Test
    public void addMemberToStudy() {
        Long memberId = 1L;
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 1; i < 1000; i++) {
            Long studyId = (long) i;
            StudyPosition position = random.nextBoolean() ? StudyPosition.MEMBER : StudyPosition.LEADER;
            boolean permissionRecruitManage = random.nextBoolean();
            boolean permissionArticleManage = random.nextBoolean();
            boolean permissionCalendarManage = random.nextBoolean();
            boolean permissionSettingManage = random.nextBoolean();

            studyMemberService.addMemberToStudy(memberId, studyId, position,
                    permissionRecruitManage, permissionArticleManage, permissionCalendarManage, permissionSettingManage);
        }
    }
}
