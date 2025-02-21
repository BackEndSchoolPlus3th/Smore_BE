package com.meossamos.smore.domain.main.main.controller;

import com.meossamos.smore.domain.main.main.entity.Main;
import com.meossamos.smore.domain.main.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ApiV1MainController {
    private final MainService mainService;

    @PostMapping("/test")
    public ResponseEntity<?> test() {

        Main main = Main.builder()
                .title("test")
                .content("test")
                .build();

        return ResponseEntity.ok(mainService.saveArticle(main));
    }
}
