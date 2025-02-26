package com.meossamos.smore.domain.study.hashTag.repository;

import com.meossamos.smore.domain.study.hashTag.entity.StudyHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyHashTagRepository extends JpaRepository<StudyHashTag, Long> {
}
