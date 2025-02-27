package com.meossamos.smore.domain.study.schedule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class AddStudyScheduleDto {
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @JsonCreator
    public AddStudyScheduleDto(
            @JsonProperty("title") String title,
            @JsonProperty("content") String content,
            @JsonProperty("startDate") String startDate,
            @JsonProperty("endDate") String endDate){
            this.title = title;
            this.content = content;
            this.startDate = parseDateTime(startDate);
            this.endDate = parseDateTime(endDate);
    }
    @Override
    public String toString() {
        return "AddStudyScheduleDto{" +
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
