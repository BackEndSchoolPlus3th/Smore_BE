package com.meossamos.smore.domain.study.document.repository;

import com.meossamos.smore.domain.study.document.entity.StudyDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyDocumentRepository extends JpaRepository<StudyDocument, Long> {
    List<StudyDocument> findByStudyId(Long study_Id);
}
