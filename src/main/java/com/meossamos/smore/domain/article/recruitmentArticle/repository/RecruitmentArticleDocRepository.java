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

}
