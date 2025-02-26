package com.meossamos.smore.domain.member.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.meossamos.smore.domain.member.member.entity.MemberDoc;
import com.meossamos.smore.domain.member.member.repository.MemberDocRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.boot.test.context.SpringBootTest;

@DataElasticsearchTest
public class MemberDocTest {
    @Autowired
    private MemberDocRepository memberDocRepository;

    @Test
    public void testSaveAndFind() {
        // 저장 테스트
        MemberDoc memberDoc = MemberDoc.builder()
                .id("3")
                .name("홍길동")
                .email("testEmail")
                .profileImageUrl("testProfileImageUrl")
                .build();
        memberDocRepository.save(memberDoc);

        // 조회 테스트
        MemberDoc savedMemberDoc = memberDocRepository.findById("3").get();
        assertThat(savedMemberDoc).isNotNull();
        assertThat(savedMemberDoc.getName()).isEqualTo("홍길동");
    }
}
