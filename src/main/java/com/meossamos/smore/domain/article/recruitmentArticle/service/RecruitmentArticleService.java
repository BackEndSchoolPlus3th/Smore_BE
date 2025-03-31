package com.meossamos.smore.domain.article.recruitmentArticle.service;

import com.meossamos.smore.domain.article.recruitmentArticle.dto.SimpleRecruitmentDto;
import com.meossamos.smore.domain.article.recruitmentArticle.dto.UpdateRecruitmentArticleDto;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.repository.RecruitmentArticleDocRepository;
import com.meossamos.smore.domain.article.recruitmentArticle.repository.RecruitmentArticleRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.global.util.HashTagUtil;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleService {
    private final RecruitmentArticleRepository recruitmentArticleRepository;
    private final RecruitmentArticleDocRepository recruitmentArticleDocRepository;
    private final MemberService memberService;
    private final StudyService studyService;

    public RecruitmentArticle save(String title, String content, String introduction, @Nullable String region, @Nullable String thumbnailUrl, @Nullable String imageUrls, LocalDate startDate, LocalDate endDate, Boolean isRecruiting, Integer maxMember, List<String> hashTags, Long memberId, Long studyId, Integer clipCount) {
        Member member = memberService.getReferenceById(memberId);
        Study study = studyService.getReferenceById(studyId);
        String hashTagsString = HashTagUtil.convertHashTagsToString(hashTags);

        RecruitmentArticle recruitmentArticle = RecruitmentArticle.builder()
                .title(title)
                .content(content)
                .introduction(introduction)
                .region(region)
                .thumbnailUrl(thumbnailUrl)
                .imageUrls(imageUrls)
                .startDate(startDate)
                .endDate(endDate)
                .isRecruiting(isRecruiting)
                .maxMember(maxMember)
                .hashTags(hashTagsString)
                .member(member)
                .study(study)
                .clipCount(clipCount)
                .build();

        return recruitmentArticleRepository.save(recruitmentArticle);
    }

    public RecruitmentArticle updateRecruitmentArticleHashTags(Long articleId, String newHashTags) {
        RecruitmentArticle article = recruitmentArticleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("RecruitmentArticle not found"));

        // 기존 해시태그와 새 해시태그를 병합하여 업데이트
        String mergedHashTags = HashTagUtil.mergeHashTags(article.getHashTags(), newHashTags);
        article.setHashTags(mergedHashTags);

        return recruitmentArticleRepository.save(article);
    }

    public RecruitmentArticle findById(Long id) {
        return recruitmentArticleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RecruitmentArticle not found"));
    }

    @Transactional
    public void updateClipCounter(RecruitmentArticle recruitmentArticle, String upOrDown) {
        Integer clipCount = recruitmentArticle.getClipCount();
        if (upOrDown.equals("up")) {
            clipCount++;
        } else if (upOrDown.equals("down")) {
            clipCount--;
        }
        recruitmentArticle.setClipCount(clipCount);
    }

    public RecruitmentArticle findByIdWithMember(Long id) {
        return recruitmentArticleRepository.findByIdWithMemberAndStudyLeader(id)
                .orElseThrow(() -> new EntityNotFoundException("RecruitmentArticle not found"));
    }

    public List<SimpleRecruitmentDto> findByStudyId(Long studyId) {
        return recruitmentArticleRepository.findSimpleRecruitmentsByStudyId(studyId);
    }

    // 게시글 수정
    @Transactional
    public RecruitmentArticle updateRecruitmentArticle(Long articleId, Long requestingMemberId, UpdateRecruitmentArticleDto dto) {
        // 한 번의 join fetch 쿼리로 게시글 작성자와 스터디 리더 정보 조회
        RecruitmentArticle article = recruitmentArticleRepository.findByIdWithMemberAndStudyLeader(articleId)
                .orElseThrow(() -> new EntityNotFoundException("RecruitmentArticle not found"));

        // 요청한 사용자가 게시글 작성자이거나 스터디 리더인지 확인
        if (!article.getMember().getId().equals(requestingMemberId) &&
                !article.getStudy().getLeader().getId().equals(requestingMemberId)) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }

        // DTO 값으로 게시글 필드 업데이트
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setIntroduction(dto.getIntroduction());
        article.setRegion(dto.getRegion());
        article.setThumbnailUrl(dto.getThumbnailUrl());
        article.setImageUrls(String.join(",", dto.getImageUrls()));
        article.setStartDate(dto.getStartDate());
        article.setEndDate(dto.getEndDate());
        article.setIsRecruiting(dto.getIsRecruiting());
        article.setMaxMember(dto.getMaxMember());
        article.setHashTags(HashTagUtil.convertHashTagsToString(dto.getHashTags()));

        return recruitmentArticleRepository.save(article);
    }


    /**
     * 게시글 삭제: 요청한 사용자가 게시글 작성자이거나 스터디 리더인 경우 삭제.
     * MySQL DB에서 삭제 후, Elasticsearch에서도 해당 문서를 삭제함.
     */
    @Transactional
    public void deleteRecruitmentArticle(Long articleId, Long requestingMemberId) {
        RecruitmentArticle article = recruitmentArticleRepository.findByIdWithMemberAndStudyLeader(articleId)
                .orElseThrow(() -> new EntityNotFoundException("RecruitmentArticle not found"));

        // 요청한 사용자가 게시글 작성자이거나, 스터디의 리더인 경우만 삭제 가능
        if (!article.getMember().getId().equals(requestingMemberId) &&
                !article.getStudy().getLeader().getId().equals(requestingMemberId)) {
            throw new AccessDeniedException("삭제할 권한이 없습니다.");
        }

        recruitmentArticleRepository.delete(article);
        System.out.println("Deleted article from MySQL DB");

        // Elasticsearch에서도 해당 도큐먼트를 삭제
        try {
            recruitmentArticleDocRepository.deleteById(String.valueOf(article.getId()));
        } catch (Exception e) {
            System.out.println("Failed to delete document from Elasticsearch: " + e.getMessage());
        }
    }
}
