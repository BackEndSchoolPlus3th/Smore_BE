package com.meossamos.smore.domain.article.studyArticle.service;

import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.article.studyArticle.repository.StudyArticleRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyArticleService {
    private final StudyArticleRepository studyArticleRepository;

    public StudyArticle saveStudyArticle(String title, String content, @Nullable String imageUrls, @Nullable String attachments, @Nullable String hashTags, Study study, Member member) {
        StudyArticle studyArticle = StudyArticle.builder()
                .title(title)
                .content(content)
                .imageUrls(imageUrls)
                .attachments(attachments)
                .hashTags(hashTags)
                .study(study)
                .member(member)
                .build();

        return studyArticleRepository.save(studyArticle);
    }
}
