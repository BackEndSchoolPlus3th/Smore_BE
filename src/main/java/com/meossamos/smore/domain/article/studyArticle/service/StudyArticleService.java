package com.meossamos.smore.domain.article.studyArticle.service;

import com.meossamos.smore.domain.article.studyArticle.dto.StudyArticleDto;
import com.meossamos.smore.domain.article.studyArticle.dto.request.StudyArticleCreateRequest;
import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.article.studyArticle.repository.StudyArticleRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyArticleService {
    private final StudyArticleRepository studyArticleRepository;
    private final StudyRepository studyRepository;

    public StudyArticle saveStudyArticle(String title, String content, @Nullable String imageUrls, @Nullable String attachments, @Nullable String hashTags,  Member member, Study study) {
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

    private StudyArticleDto convertToStudyArticleDto(StudyArticle studyArticle, boolean isDetail) {
        StudyArticleDto.StudyArticleDtoBuilder dtoBuilder = StudyArticleDto.builder()
                .id(studyArticle.getId())
                .title(studyArticle.getTitle())
                .content(studyArticle.getContent());

        if (isDetail) {
            dtoBuilder.member(studyArticle.getMember())
                    .attachments(studyArticle.getAttachments());
        }

        return dtoBuilder.build();
    }

    // 게시글 조회
    public List<StudyArticleDto> getArticlesByStudyId(Long studyId) {
        List<StudyArticle> articles = studyArticleRepository.findByStudyId(studyId);
        return articles.stream()
                .map(article -> convertToStudyArticleDto(article, false))
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    public StudyArticleDto getStudyArticleById(Long articleId) {
        StudyArticle studyArticle = studyArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return convertToStudyArticleDto(studyArticle, true);
    }

    // 게시글 작성
    public StudyArticleDto createStudyArticle(Long studyId, StudyArticleCreateRequest createRequest, Member member) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 스터디 ID입니다."));

        StudyArticle studyArticle = StudyArticle.builder()
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .attachments(createRequest.getAttachments())
                .study(study)
                .member(member)
                .build();

        StudyArticle savedArticle = studyArticleRepository.save(studyArticle);

        return convertToStudyArticleDto(savedArticle);
    }

    // 게시글 수정
    public StudyArticleDto updateStudyArticle(Long articleId, StudyArticleDto updateRequest) {
        StudyArticle studyArticle = studyArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        studyArticle.setTitle(updateRequest.getTitle());
        studyArticle.setContent(updateRequest.getContent());
        studyArticle.setAttachments(updateRequest.getAttachments());

        StudyArticle updatedArticle = studyArticleRepository.save(studyArticle);

        return convertToStudyArticleDto(updatedArticle);
    }

    // 게시글 삭제
    public void deleteStudyArticle(Long articleId) {
        StudyArticle studyArticle = studyArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        studyArticleRepository.delete(studyArticle);
    }

    // DTO로 변환
    private StudyArticleDto convertToStudyArticleDto(StudyArticle studyArticle) {
        return StudyArticleDto.builder()
                .id(studyArticle.getId())
                .title(studyArticle.getTitle())
                .content(studyArticle.getContent())
                .member(studyArticle.getMember())
                .build();
    }
}
