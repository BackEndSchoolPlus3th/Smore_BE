package com.meossamos.smore.domain.home.article.controller;

import com.meossamos.smore.domain.home.article.entity.Article;
import com.meossamos.smore.domain.home.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ApiV1ArticleController {
    private final ArticleService articleService;

    @PostMapping("/test")
    public ResponseEntity<?> test() {

        Article article = Article.builder()
                .title("test")
                .content("test")
                .build();

        return ResponseEntity.ok(articleService.saveArticle(article));
    }
}
