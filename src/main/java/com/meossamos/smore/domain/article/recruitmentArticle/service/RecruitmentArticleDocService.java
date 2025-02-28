package com.meossamos.smore.domain.article.recruitmentArticle.service;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.FieldSort;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticleDoc;
import com.meossamos.smore.global.util.ElasticSearchUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleDocService {

    private final ElasticSearchUtil elasticSearchUtil;
    private static final int BLOCK_SIZE = 10;
    private static final String ES_INDEX_NAME = "es_recruitment_article";
    private final Map<String, ElasticSearchUtil.SearchResult<RecruitmentArticleDoc>> blockCache = new HashMap<>();


    /**
     * 해시태그를 이용한 모집글 검색(페이징)
     *
     * @param hashTagList 검색에 사용할 해시태그 리스트
     * @param pageNum     현재 페이지 번호 (0부터 시작)
     * @param pageSize    페이지당 도큐먼트 수
     * @return 페이징 처리된 RecruitmentArticleDoc 페이지
     */
    public List<RecruitmentArticleDoc> findByHashTags(List<String> hashTagList, int pageNum, int pageSize) {
        // 1. 1-indexed 페이지 번호를 0-indexed로 변환
        int adjustedPageNum = pageNum - 1;
        List<RecruitmentArticleDoc> result = new ArrayList<>();

        // 2. 블록 번호 계산 (예: BLOCK_SIZE = 10)
        int blockNumber = adjustedPageNum / BLOCK_SIZE;
        // 블록의 시작 페이지 (0-indexed)
        int blockStartPage = blockNumber * BLOCK_SIZE;
        // 블록의 시작 도큐먼트 인덱스
        int pageStart = blockStartPage * pageSize;
        // 블록 내 총 도큐먼트 개수
        int searchSize = pageSize * BLOCK_SIZE;

        System.out.println("blockNumber: " + blockNumber);
        System.out.println("blockStartPage: " + blockStartPage);
        System.out.println("pageStart: " + pageStart);
        System.out.println("searchSize: " + searchSize);

        // 캐시 키 구성 (간단하게 해시태그 리스트와 blockNumber 결합)
        String cacheKey = hashTagList.toString() + "_block_" + blockNumber;
        ElasticSearchUtil.SearchResult<RecruitmentArticleDoc> searchResult;

        if (blockCache.containsKey(cacheKey)) {
            // 캐시된 결과 사용
            searchResult = blockCache.get(cacheKey);
        } else {
            // Elasticsearch에서 해당 블록 검색 후 캐시 저장
            searchResult = searchByHashTagList(hashTagList, pageStart, searchSize);
            blockCache.put(cacheKey, searchResult);
        }

        // 페이징 처리
        int startIdx = adjustedPageNum % BLOCK_SIZE * pageSize;
        int endIdx = Math.min(startIdx + pageSize, searchResult.getDocs().size());
        for (int i = startIdx; i < endIdx; i++) {
            RecruitmentArticleDoc doc = searchResult.getDocs().get(i);
            result.add(doc);
        }

        return result;
    }

    /**
     * 해시태그 리스트로 Elasticsearch 쿼리를 구성하여 모집글 검색을 수행
     *
     * @param hashTagList 해시태그 리스트
     * @param startNum    검색 시작 인덱스
     * @param size        검색할 도큐먼트 개수
     * @return 검색 결과(SearchResult) 객체
     */
    private ElasticSearchUtil.SearchResult<RecruitmentArticleDoc> searchByHashTagList(List<String> hashTagList, int startNum, int size) {
        // bool 쿼리 생성
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // match_all 쿼리 추가
        Query matchAllQuery = new MatchAllQuery.Builder().build()._toQuery();
        boolQueryBuilder.must(matchAllQuery);

        // 각 해시태그에 대해 match 쿼리를 should 조건으로 추가
        hashTagList.forEach(hashTag -> {
            Query matchQuery = new MatchQuery.Builder()
                    .field("hash_tags")
                    .query(hashTag)
                    .build()
                    ._toQuery();
            boolQueryBuilder.should(matchQuery);
        });

        Query boolQuery = boolQueryBuilder.build()._toQuery();

        // 정렬 기준 설정
        FieldSort sortScore = FieldSort.of(b -> b.field("_score").order(SortOrder.Desc));
        FieldSort sortUpdateDate = FieldSort.of(b -> b.field("updated_date").order(SortOrder.Desc));
        List<SortOptions> sortOptionsList = List.of(
                new SortOptions.Builder().field(sortScore).build(),
                new SortOptions.Builder().field(sortUpdateDate).build()
        );

        try {
            return elasticSearchUtil.searchByQuery(
                    ES_INDEX_NAME,
                    boolQuery,
                    RecruitmentArticleDoc.class,
                    startNum,
                    size,
                    sortOptionsList
            );
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 발생 시 빈 결과 반환
            return new ElasticSearchUtil.SearchResult<>(new ArrayList<>(), 0);
        }
    }
}
