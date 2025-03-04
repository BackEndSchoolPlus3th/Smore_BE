package com.meossamos.smore.domain.study.schedule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class UpdateStudyScheduleDto {
    private Long id;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private boolean allDay;

    @JsonCreator
    public UpdateStudyScheduleDto(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("content") String content,
            @JsonProperty("startDate") String startDate,
            @JsonProperty("endDate") String endDate,
            @JsonProperty("allDay") Boolean allDay){
        this.id = id;
        this.title = title;
        this.content = content;
        this.startDate = parseDateTime(startDate);
        this.endDate = parseDateTime(endDate);
        this.allDay = allDay;
    }

    @Override
    public String toString() {
        return "UpdateStudyScheduleDto{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }

    private LocalDateTime parseDateTime(String dateTimeStr){
        if(dateTimeStr.length() == 10){ // 날짜만 들어온 경우
            return LocalDateTime.parse(dateTimeStr + "T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
    }
}
