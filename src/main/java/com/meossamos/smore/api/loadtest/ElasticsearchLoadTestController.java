package com.meossamos.smore.api.loadtest;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticleDoc;
import com.meossamos.smore.domain.article.recruitmentArticle.repository.RecruitmentArticleRepository;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleDocService;
import com.meossamos.smore.global.util.ElasticSearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/loadtest")
@RequiredArgsConstructor
public class ElasticsearchLoadTestController {

    private final RecruitmentArticleRepository recruitmentArticleRepository;
    private final RecruitmentArticleDocService recruitmentArticleDocService;

    /**
     * MySQL에 저장된 recruitment_article 테이블의 데이터를 기반으로
     * Elasticsearch 검색 API의 평균 응답시간을 측정합니다.
     *
     * @param iterations 부하 테스트 반복 횟수 (기본값: 100)
     * @return 반복 횟수와 전체, 평균 응답시간을 JSON 형식으로 반환
     */
    @GetMapping("/elasticsearch")
    public ResponseEntity<?> loadTestElasticsearch(
            @RequestParam(name = "iterations", defaultValue = "100") int iterations) {

        // MySQL에 저장된 모든 채용글 조회
        List<RecruitmentArticle> articles = recruitmentArticleRepository.findAll();
        if (articles == null || articles.isEmpty()) {
            return ResponseEntity.badRequest().body("MySQL에 등록된 채용글이 없습니다.");
        }

        long totalTimeMs = 0;
        int count = 0;

        for (int i = 0; i < iterations; i++) {
            // 라운드 로빈 방식으로 채용글 선택
            RecruitmentArticle article = articles.get(i % articles.size());

            // Elasticsearch 검색 조건 구성
            List<String> titleList = List.of(article.getTitle());
            List<String> contentList = List.of(article.getContent());
            List<String> introductionList = List.of(article.getIntroduction());
            List<String> regionList = List.of(article.getRegion());

            // 해시태그는 DB에 CSV 형태로 저장된 경우 split 처리 (예: "java,spring,elasticsearch")
            List<String> hashTagList = List.of(article.getHashTags().split(","));

            long startTime = System.currentTimeMillis();
            ElasticSearchUtil.SearchResult<RecruitmentArticleDoc> result =
                    recruitmentArticleDocService.findByTitleOrContentOrIntroductionOrRegionOrHashTags(
                            titleList,
                            contentList,
                            introductionList,
                            regionList,
                            hashTagList,
                            1,    // 페이지 번호 (예시)
                            10,   // 페이지 사이즈 (예시)
                            false // 사용자 맞춤 추천 여부
                    );
            long endTime = System.currentTimeMillis();

            totalTimeMs += (endTime - startTime);
            count++;
        }

        double avgResponseTime = (double) totalTimeMs / count;

        Map<String, Object> response = new HashMap<>();
        response.put("iterations", iterations);
        response.put("totalTimeMs", totalTimeMs);
        response.put("averageResponseTimeMs", avgResponseTime);

        return ResponseEntity.ok(response);
    }
}
