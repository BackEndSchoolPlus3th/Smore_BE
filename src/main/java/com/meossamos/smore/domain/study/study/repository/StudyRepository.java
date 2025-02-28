package com.meossamos.smore.domain.study.study.repository;

import com.meossamos.smore.domain.study.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
}
