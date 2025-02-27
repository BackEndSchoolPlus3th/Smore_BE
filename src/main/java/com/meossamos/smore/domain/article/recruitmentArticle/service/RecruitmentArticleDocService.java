package com.meossamos.smore.domain.article.recruitmentArticle.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ObjectBuilder;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticleDoc;
import com.meossamos.smore.domain.article.recruitmentArticle.repository.RecruitmentArticleDocRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleDocService {
    private final RecruitmentArticleDocRepository recruitmentArticleDocRepository;

    @Value("${custom.elasticsearch.uri}")
    private String elasticsearchUri;

    // 해시태그를 이용한 모집글 검색(페이징)
    public Page<RecruitmentArticleDoc> findByHashTags(List<String> hashTagList, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<RecruitmentArticleDoc> recruitmentArticleDocPage = null;

        List<RecruitmentArticleDoc> recruitmentArticleDocList = new ArrayList<>();

        // bool 쿼리 생성
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // match_all 쿼리 생성
        Query matchAllQuery = new MatchAllQuery.Builder().build()._toQuery();
        boolQueryBuilder.must(matchAllQuery);

        // match 쿼리 생성
        hashTagList.forEach(hashTag -> {
            Query matchQuery = new MatchQuery.Builder()
                    .field("hash_tags")
                    .query(hashTag)
                    .build()
                    ._toQuery();

            boolQueryBuilder.should(matchQuery);
        });

        Query boolQuery = boolQueryBuilder.build()._toQuery();


        // 정렬 기준
        FieldSort sort_Score = FieldSort.of(b -> b
                .field("_score")
                .order(SortOrder.Desc)
        );

        FieldSort sortUpdateDate = FieldSort.of(b -> b
                .field("updated_date")
                .order(SortOrder.Desc)
        );

        List<SortOptions> sortOptionsList = new ArrayList<>(
                List.of(
                        new SortOptions.Builder()
                                .field(sort_Score)
                                .build(),
                        new SortOptions.Builder()
                                .field(sortUpdateDate)
                                .build()
                )
        );


        RestClient restClient = RestClient
                .builder(HttpHost.create(elasticsearchUri))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        try {
            SearchResponse<RecruitmentArticleDoc> response = esClient.search(s -> s
                            .index("es_recruitment_article")
                            .from(pageNum * pageSize)
                            .size(pageSize * 10)
                            .sort(sortOptionsList)
                            .query(boolQuery),
                    RecruitmentArticleDoc.class
            );

            // 검색 결과 총 개수 출력
            System.out.println("Total hits: " + response.hits().total());

            // 각 문서를 순회하면서 출력하고 리스트에 추가
            for (Hit<RecruitmentArticleDoc> hit : response.hits().hits()) {
                RecruitmentArticleDoc doc = hit.source();
                System.out.println("Document: " + Objects.requireNonNull(doc).getId());
                recruitmentArticleDocList.add(doc);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return recruitmentArticleDocPage;
    }

    public List<RecruitmentArticleDoc> findAllByContent(String content) {
        return recruitmentArticleDocRepository.customQueryFindAll();
    }

    public List<RecruitmentArticleDoc> searchTest() throws IOException {
        List<RecruitmentArticleDoc> recruitmentArticleDocList = new ArrayList<>();

        // size 설정
        int size = 20;

//         sort 설정
        String sortField = "createdAt";
        SortOrder sortOrder = SortOrder.Desc;

        // match_all 쿼리 생성
        Query matchAllQuery = new MatchAllQuery.Builder().build()._toQuery();

        // match 쿼리 생성
        Query matchQuery = new MatchQuery.Builder()
                .field("hash_tags")
                .query("백엔드")
                .build()
                ._toQuery();
        Query matchQuery2 = new MatchQuery.Builder()
                .field("hash_tags")
                .query("postgresql")
                .build()
                ._toQuery();
        Query matchQuery3 = new MatchQuery.Builder()
                .field("hash_tags")
                .query("프론트")
                .build()
                ._toQuery();

        // bool 쿼리 생성
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        boolQueryBuilder.must(matchAllQuery);
        boolQueryBuilder.should(matchQuery);
        boolQueryBuilder.should(matchQuery2);
        boolQueryBuilder.should(matchQuery3);

        Query boolQuery = boolQueryBuilder.build()._toQuery();

        FieldSort sort_Score = FieldSort.of(b -> b
                .field("_score")
                .order(SortOrder.Desc)
        );

        FieldSort sortUpdateDate = FieldSort.of(b -> b
                .field("updated_date")
                .order(SortOrder.Desc)
        );

        List<SortOptions> sortOptionsList = new ArrayList<>(
                List.of(
                        new SortOptions.Builder()
                                .field(sort_Score)
                                .build(),
                        new SortOptions.Builder()
                                .field(sortUpdateDate)
                                .build()
                )
        );

        RestClient restClient = RestClient
                .builder(HttpHost.create(elasticsearchUri))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        SearchResponse<RecruitmentArticleDoc> response = esClient.search(s -> s
                .index("es_recruitment_article")
                        .size(size)
                .query(boolQuery)
                        .sort(sortOptionsList),
                RecruitmentArticleDoc.class
        );

        // 검색 결과 총 개수 출력
        System.out.println("Total hits: " + response.hits().total());

        // 각 문서를 순회하면서 출력하고 리스트에 추가
        for (Hit<RecruitmentArticleDoc> hit : response.hits().hits()) {
            RecruitmentArticleDoc doc = hit.source();
            System.out.println("Document: " + Objects.requireNonNull(doc).getId());
            recruitmentArticleDocList.add(doc);
        }


        return recruitmentArticleDocList;
    }
}
