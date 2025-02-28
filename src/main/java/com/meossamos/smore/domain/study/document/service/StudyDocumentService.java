package com.meossamos.smore.domain.study.document.service;

import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.study.document.entity.StudyDocument;
import com.meossamos.smore.domain.study.document.repository.StudyDocumentRepository;
import com.meossamos.smore.domain.study.study.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyDocumentService {
    private final StudyDocumentRepository studyDocumentRepository;

    public StudyDocument saveStudyDocument(String name, String url, String type, Study study, StudyArticle studyArticle) {
        StudyDocument studyDocument = StudyDocument.builder()
                .name(name)
                .url(url)
                .type(type)
                .study(study)
                .studyArticle(studyArticle)
                .build();

        return studyDocumentRepository.save(studyDocument);
    }
}
