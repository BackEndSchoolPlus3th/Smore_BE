package com.meossamos.smore.domain.article.recruitmentArticle.repository;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticleDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentArticleDocRepository extends ElasticsearchRepository<RecruitmentArticleDoc, String> {
    @Query("""
            {
                "match_all": {}
            }
            """)
    Page<RecruitmentArticleDoc> findByHashTags(List<String> hashTagList, Pageable pageable, int pageNum, int pageSize);

//    @Query("""
//{
//  "bool": {
//    "must": { "match_all": {} },
//    "should": [
//      { "match": { "hash_tags": "백엔드" } },
//      { "match": { "hash_tags": "postgresql" } },
//      { "match": { "hash_tags": "react" } },
//      { "match": { "hash_tags": "프론트" } },
//      { "match": { "hash_tags": "" } }
//    ]
//  }
//}
//""")
    @Query(value = """
{
  "size": 20,
  "query": {
    "bool": {
      "must": {
        "match_all": {}
      },
      "should": [
        { "match": { "hash_tags": "백엔드" } },
        { "match": { "hash_tags": "postgresql" } },
        { "match": { "hash_tags": "react" } },
        { "match": { "hash_tags": "프론트" } },
        { "match": { "hash_tags": "" } }
      ]
    }
  },
  "sort": [
    { "_score": { "order": "desc" } },
    { "updated_date": { "order": "desc" } }
  ]
}
            """)
    List<RecruitmentArticleDoc> customQueryFindAllByContents(String content);


    @Query("""
            {
                "match_all": {}
            }
            """)
    List<RecruitmentArticleDoc> customQueryFindAll();
}
