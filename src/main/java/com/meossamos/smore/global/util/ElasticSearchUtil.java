package com.meossamos.smore.global.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticSearchUtil {

    @Value("${custom.elasticsearch.uri}")
    private String elasticsearchUri;

    // ElasticsearchClient를 생성하는 메서드
    private ElasticsearchClient createClient() {
        RestClient restClient = RestClient.builder(HttpHost.create(elasticsearchUri)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    /**
     * 기본 from=0, size=10, sortOptions=null을 사용하는 검색 메서드.
     *
     * @param index 검색할 인덱스 이름
     * @param query Elasticsearch 쿼리 객체
     * @param clazz 반환받을 도큐먼트 클래스 타입
     * @param <T>   도큐먼트 타입
     * @return 검색 결과를 포함한 SearchResult<T> 객체
     * @throws IOException Elasticsearch 요청 중 발생할 수 있는 예외
     */
    public <T> SearchResult<T> searchByQuery(String index, Query query, Class<T> clazz) throws IOException {
        return searchByQuery(index, query, clazz, 0, 100, null);
    }

    /**
     * 기본 sortOptions=null을 사용하는 검색 메서드.
     *
     * @param index 검색할 인덱스 이름
     * @param query Elasticsearch 쿼리 객체
     * @param clazz 반환받을 도큐먼트 클래스 타입
     * @param from  검색 시작 번호 (페이징용)
     * @param size  검색할 도큐먼트 개수
     * @param <T>   도큐먼트 타입
     * @return 검색 결과를 포함한 SearchResult<T> 객체
     * @throws IOException Elasticsearch 요청 중 발생할 수 있는 예외
     */
    public <T> SearchResult<T> searchByQuery(String index, Query query, Class<T> clazz, int from, int size) throws IOException {
        return searchByQuery(index, query, clazz, from, size, null);
    }

    /**
     * 주어진 인덱스와 쿼리, 정렬 옵션을 사용하여 Elasticsearch에 검색 요청을 보내고,
     * 결과를 SearchResult<T> 형태로 반환한다.
     *
     * @param index       검색할 인덱스 이름
     * @param query       Elasticsearch 쿼리 객체
     * @param clazz       반환받을 도큐먼트 클래스 타입
     * @param from        검색 시작 번호 (페이징용)
     * @param size        검색할 도큐먼트 개수
     * @param sortOptions 정렬 옵션 리스트 (null일 경우 정렬 조건을 적용하지 않음)
     * @param <T>         도큐먼트 타입
     * @return 검색 결과를 포함한 SearchResult<T> 객체
     * @throws IOException Elasticsearch 요청 중 발생할 수 있는 예외
     */
    public <T> SearchResult<T> searchByQuery(String index, Query query, Class<T> clazz, int from, int size, List<SortOptions> sortOptions) throws IOException {
        ElasticsearchClient client = createClient();
        SearchResponse<T> response = client.search(s -> {
            s.index(index);
            s.from(from);
            s.size(size);
            s.query(query);
            if (sortOptions != null && !sortOptions.isEmpty()) {
                s.sort(sortOptions);
            }
            return s;
        }, clazz);

        List<T> docs = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
        long totalHits = response.hits().total().value();
        return new SearchResult<>(docs, totalHits);
    }

    // 검색 결과를 담을 제네릭 클래스
    public static class SearchResult<T> {
        private final List<T> docs;
        private final long totalHits;

        public SearchResult(List<T> docs, long totalHits) {
            this.docs = docs;
            this.totalHits = totalHits;
        }

        public List<T> getDocs() {
            return docs;
        }

        public long getTotalHits() {
            return totalHits;
        }
    }
}
