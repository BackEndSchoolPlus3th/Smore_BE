package com.meossamos.smore.domain.main.main.service;

import com.meossamos.smore.domain.main.main.entity.Main;
import com.meossamos.smore.domain.main.main.repository.MainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainService {
    private final MainRepository mainRepository;

    public Main saveArticle(Main main) {
        return mainRepository.save(main);
    }
}
