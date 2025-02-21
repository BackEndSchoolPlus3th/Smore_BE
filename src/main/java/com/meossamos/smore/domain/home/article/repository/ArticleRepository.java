package com.meossamos.smore.domain.home.article.repository;

import com.meossamos.smore.domain.home.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
