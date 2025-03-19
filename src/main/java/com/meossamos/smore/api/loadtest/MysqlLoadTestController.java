package com.meossamos.smore.api.loadtest;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.repository.RecruitmentArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/loadtest")
@RequiredArgsConstructor
public class MysqlLoadTestController {

    private final RecruitmentArticleRepository recruitmentArticleRepository;

    /**
     * MySQL 검색 성능 테스트 API
     *
     * Elasticsearch와 동일한 검색 조건(제목, 내용, 소개, 지역, 해시태그)을 사용하여
     * MySQL에서 데이터를 조회하는 성능(총 시간, 평균 응답시간)을 측정합니다.
     *
     * @param iterations 부하 테스트 반복 횟수 (기본값: 100)
     * @return 반복 횟수와 전체, 평균 응답시간을 JSON 형식으로 반환
     */
    @GetMapping("/mysql")
    public ResponseEntity<?> loadTestMySQL(
            @RequestParam(name = "iterations", defaultValue = "100") int iterations) {

        // MySQL에 저장된 모든 채용글을 조회합니다.
        List<RecruitmentArticle> articles = recruitmentArticleRepository.findAll();
        if (articles == null || articles.isEmpty()) {
            return ResponseEntity.badRequest().body("MySQL에 등록된 채용글이 없습니다.");
        }

        long totalTimeMs = 0;
        int count = 0;

        for (int i = 0; i < iterations; i++) {
            // 라운드 로빈 방식으로 채용글을 선택합니다.
            RecruitmentArticle article = articles.get(i % articles.size());

            // 검색 조건 구성: Elasticsearch와 동일하게 제목, 내용, 소개, 지역, 해시태그 조건 사용
            String title = article.getTitle();
            String content = article.getContent();
            String introduction = article.getIntroduction();
            String region = article.getRegion();
            String hashTags = article.getHashTags(); // CSV 형태의 문자열

            long startTime = System.currentTimeMillis();
            // 커스텀 쿼리 메서드를 통해 MySQL에서 검색을 수행합니다.
            List<RecruitmentArticle> results = recruitmentArticleRepository
                    .findByTitleContainingOrContentContainingOrIntroductionContainingOrRegionContainingOrHashTagsContaining(
                            title, content, introduction, region, hashTags);
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
