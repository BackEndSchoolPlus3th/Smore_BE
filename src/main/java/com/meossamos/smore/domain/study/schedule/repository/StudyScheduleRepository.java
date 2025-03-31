package com.meossamos.smore.domain.study.schedule.repository;

import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {
    List<StudySchedule> findByStudy_Id(Long studyId);
}
