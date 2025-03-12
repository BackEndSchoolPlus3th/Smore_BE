package com.meossamos.smore.domain.article.studyArticle.service;

import com.meossamos.smore.domain.article.studyArticle.dto.StudyArticleDto;
import com.meossamos.smore.domain.article.studyArticle.dto.request.StudyArticleCreateRequest;
import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.article.studyArticle.repository.StudyArticleRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import com.meossamos.smore.global.s3.S3Service;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyArticleService {
    private final StudyArticleRepository studyArticleRepository;
    private final StudyRepository studyRepository;
    private final StudyMemberService studyMemberService;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public StudyArticle saveStudyArticle(String title, String content, @Nullable String imageUrls, @Nullable List<String> attachments, @Nullable String hashTags,  Member member, Study study) {
        StudyArticle studyArticle = StudyArticle.builder()
                .title(title)
                .content(content)
                .imageUrls(imageUrls)
                .attachments(attachments)
                .study(study)
                .member(member)
                .build();

        return studyArticleRepository.save(studyArticle);
    }

    // 게시글 조회
    @Transactional
    public List<StudyArticleDto> getArticlesByStudyId(Long studyId) {
        List<StudyArticle> articles = studyArticleRepository.findByStudyId(studyId, Sort.by(Sort.Order.desc("createdDate")));
        return articles.stream()
                .map(article -> convertToStudyArticleDto(article))
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    @Transactional
    public StudyArticleDto getStudyArticleById(Long articleId) {
        StudyArticle studyArticle = studyArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return convertToStudyArticleDto(studyArticle);
    }

    // 게시글 작성
    @Transactional
    public StudyArticleDto createStudyArticle(Long studyId, StudyArticleCreateRequest createRequest, String fileUrl) throws IOException {
        Long memberId = studyMemberService.getAuthenticatedMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("인증된 사용자가 없습니다."));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 스터디 ID입니다."));

        StudyArticle studyArticle = StudyArticle.builder()
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .member(member)
                .study(study)
                .build();

        // 게시글 저장
        StudyArticle savedArticle = studyArticleRepository.save(studyArticle);

        if (fileUrl != null && !fileUrl.isEmpty()) {
            savedArticle.setImageUrls(fileUrl);  // 전달받은 S3 URL을 게시글에 저장
            studyArticleRepository.save(savedArticle);  // 게시글 업데이트
        }

        // DTO 변환 후 반환
        return convertToStudyArticleDto(savedArticle);
    }

    // 게시글 수정
    @Transactional
    public StudyArticleDto updateStudyArticle(Long articleId, StudyArticleDto updateRequest) {
        Long memberId = studyMemberService.getAuthenticatedMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("인증된 사용자가 없습니다."));


        StudyArticle studyArticle = studyArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 작성자와 현재 로그인한 사용자의 id 비교
        if (!studyArticle.getMember().equals(member)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        studyArticle.setTitle(updateRequest.getTitle());
        studyArticle.setContent(updateRequest.getContent());

        StudyArticle updatedArticle = studyArticleRepository.save(studyArticle);

        return convertToStudyArticleDto(updatedArticle);
    }

    // 게시글 삭제
    @Transactional
    public void deleteStudyArticle(Long articleId) {
        Long memberId = studyMemberService.getAuthenticatedMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("인증된 사용자가 없습니다."));

        StudyArticle studyArticle = studyArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!studyArticle.getMember().equals(member)) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }

        studyArticleRepository.delete(studyArticle);
    }

    // 게시글 검색
    public List<StudyArticleDto> searchArticles(Long studyId, String title, String content) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 스터디 ID입니다."));

        List<StudyArticle> articles = studyArticleRepository.findByStudyAndSearchConditions(
                study, title, content);

        return articles.stream()
                .map(this::convertToStudyArticleDto)
                .collect(Collectors.toList());
    }

    // DTO로 변환
    private StudyArticleDto convertToStudyArticleDto(StudyArticle studyArticle) {
        return StudyArticleDto.builder()
                .id(studyArticle.getId())
                .title(studyArticle.getTitle())
                .content(studyArticle.getContent())
                .imageUrls(studyArticle.getImageUrls())
                .member(studyArticle.getMember())
                .build();
    }

    public StudyArticle findById(Long articleId) {
        return studyArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    public StudyArticle save(StudyArticle studyArticle) {
        return studyArticleRepository.save(studyArticle);
    }
}
