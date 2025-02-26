package com.meossamos.smore.domain.article.recruitmentArticleHashTag.repository;

import com.meossamos.smore.domain.article.recruitmentArticleHashTag.entity.RecruitmentArticleHashTagDoc;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentArticleHashTagDocRepository extends ElasticsearchRepository<RecruitmentArticleHashTagDoc, String> {
    @Query("""
    {
      "bool": {
        "must": [
          { "terms": { "hash_tag.keyword": ?0 } }
        ],
        "filter": [
          { "exists": { "field": "recruitment_article_id" } }
        ]
      }
    }
    """)
    List<RecruitmentArticleHashTagDoc> searchByHashTagsAndPage(List<String> hashTags, Pageable pageable);

}
