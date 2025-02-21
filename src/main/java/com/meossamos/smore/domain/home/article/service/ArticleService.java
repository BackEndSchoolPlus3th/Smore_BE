package com.meossamos.smore.domain.home.article.service;

import com.meossamos.smore.domain.home.article.entity.Article;
import com.meossamos.smore.domain.home.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }
}
