package com.meossamos.smore.domain.study.schedule.dto;

import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StudyScheduleDto {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;


    public StudyScheduleDto(StudySchedule studySchedule){
        this.id = studySchedule.getId();
        this.title = studySchedule.getTitle();
        this.content = studySchedule.getContent();
        this.startDate = studySchedule.getStartDate();
        this.endDate = studySchedule.getEndDate();

    }

}
