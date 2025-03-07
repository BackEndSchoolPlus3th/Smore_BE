package com.meossamos.smore.domain.article.recruitmentArticle.service;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.FieldSort;
import com.meossamos.smore.domain.article.recruitmentArticle.dto.RecruitmentArticleResponseData;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticleDoc;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleDocService {

    private final ElasticSearchUtil elasticSearchUtil;
    private static final int BLOCK_SIZE = 10;
    private static final String ES_INDEX_NAME = "es_recruitment_article";
    private final Map<String, ElasticSearchUtil.SearchResult<RecruitmentArticleDoc>> blockCache = new HashMap<>();
    private final MemberService memberService;

    // RecruitmentArticleDoc 리스트를 RecruitmentArticleResponseData 리스트로 변환하는 메서드
    public List<RecruitmentArticleResponseData> convertToResponseData(List<RecruitmentArticleDoc> recruitmentArticleDocs) {
        List<RecruitmentArticleResponseData> recruitmentArticleResponseDataList = new ArrayList<>();

        // 모든 회원 ID를 추출 (중복 제거)
        List<Long> memberIds = recruitmentArticleDocs.stream()
                .map(RecruitmentArticleDoc::getMember_id)
                .distinct()
                .collect(Collectors.toList());
        List<Member> members = memberService.findByIds(memberIds);

        // 조회된 회원들을 ID별 Map으로 변환
        Map<Long, Member> memberMap = members.stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));

        // 각 RecruitmentArticleDoc을 RecruitmentArticleResponseData로 변환
        for (RecruitmentArticleDoc recruitmentArticleDoc : recruitmentArticleDocs) {
            Member member = memberMap.get(recruitmentArticleDoc.getMember_id());
            RecruitmentArticleResponseData recruitmentArticleResponseData = RecruitmentArticleResponseData.builder()
                    .id(Long.valueOf(recruitmentArticleDoc.getId()))
                    .title(recruitmentArticleDoc.getTitle())
                    .introduction(recruitmentArticleDoc.getIntroduction())
                    .hashTags(recruitmentArticleDoc.getHash_tags())
                    .region(recruitmentArticleDoc.getRegion())
                    .thumbnailUrl(recruitmentArticleDoc.getThumbnail_url())
                    .isRecruiting(recruitmentArticleDoc.getIs_recruiting())
                    .ClipCount(recruitmentArticleDoc.getClip_count())
                    .writerName(member.getNickname())
                    .writerProfileImageUrl(member.getProfileImageUrl())
                    .build();
            recruitmentArticleResponseDataList.add(recruitmentArticleResponseData);
        }
        return recruitmentArticleResponseDataList;
    }

    // 제목, 내용, 소개, 지역, 해시태그로 검색하여 RecruitmentArticleDoc 리스트를 반환하는 메서드
    public  ElasticSearchUtil.SearchResult<RecruitmentArticleDoc> findByTitleOrContentOrIntroductionOrRegionOrHashTags(
            List<String> titleList, List<String> contentList, List<String> introductionList,
            List<String> regionList, List<String> hashTagList, int page, int size) {
        // 1. 1-indexed 페이지 번호를 0-indexed로 변환
        int adjustedPageNum = page - 1;

        // 2. 블록 번호 계산 (예: BLOCK_SIZE = 10)
        int blockNumber = adjustedPageNum / BLOCK_SIZE;
        // 블록의 시작 페이지 (0-indexed)
        int blockStartPage = blockNumber * BLOCK_SIZE;
        // 블록의 시작 도큐먼트 인덱스
        int pageStart = blockStartPage * size;
        // 블록 내 총 도큐먼트 개수
        int searchSize = size * BLOCK_SIZE;

        System.out.println("blockNumber: " + blockNumber);
        System.out.println("blockStartPage: " + blockStartPage);
        System.out.println("pageStart: " + pageStart);
        System.out.println("searchSize: " + searchSize);

        // 캐시 키 구성 (간단하게 해시태그 리스트와 blockNumber 결합)
        String cacheKey = titleList.toString() + contentList.toString() + introductionList.toString() + regionList.toString() + hashTagList.toString() + "_block_" + blockNumber + "_size_" + searchSize;
        ElasticSearchUtil.SearchResult<RecruitmentArticleDoc> searchResult;

        if (blockCache.containsKey(cacheKey)) {
            // 캐시된 결과 사용
            System.out.println("Using cache");
            searchResult = blockCache.get(cacheKey);
        } else {
            // Elasticsearch에서 해당 블록 검색 후 캐시 저장
            System.out.println("Searching from Elasticsearch");
            searchResult = searchByTitleOrContentOrIntroductionOrRegionOrHashTags(titleList, contentList, introductionList, regionList, hashTagList, pageStart, searchSize);
            blockCache.put(cacheKey, searchResult);
        }

        return searchResult;
    }

    // Elasticsearch에서 제목, 내용, 소개, 지역, 해시태그로 검색하는 메서드
    private  ElasticSearchUtil.SearchResult<RecruitmentArticleDoc> searchByTitleOrContentOrIntroductionOrRegionOrHashTags(
            List<String> titleList, List<String> contentList, List<String> introductionList,
            List<String> regionList, List<String> hashTagList, int pageStart, int searchSize) {

        // bool 쿼리 생성
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // match_all 쿼리 추가
        Query matchAllQuery = new MatchAllQuery.Builder().build()._toQuery();
        boolQueryBuilder.must(matchAllQuery);

        // 각 필드에 대해 match 쿼리를 should 조건으로 추가
        titleList.forEach(title -> {
            Query matchQuery = new MatchQuery.Builder()
                    .field("title")
                    .query(title)
                    .build()
                    ._toQuery();
            boolQueryBuilder.should(matchQuery);
        });

        contentList.forEach(content -> {
            Query matchQuery = new MatchQuery.Builder()
                    .field("content")
                    .query(content)
                    .build()
                    ._toQuery();
            boolQueryBuilder.should(matchQuery);
        });

        introductionList.forEach(introduction -> {
            Query matchQuery = new MatchQuery.Builder()
                    .field("introduction")
                    .query(introduction)
                    .build()
                    ._toQuery();
            boolQueryBuilder.should(matchQuery);
        });

        regionList.forEach(region -> {
            Query matchQuery = new MatchQuery.Builder()
                    .field("region")
                    .query(region)
                    .build()
                    ._toQuery();
            boolQueryBuilder.should(matchQuery);
        });

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
                    pageStart,
                    searchSize,
                    sortOptionsList
            );
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 발생 시 빈 결과 반환
            return new ElasticSearchUtil.SearchResult<>(new ArrayList<>(), 0);
        }
    }
}