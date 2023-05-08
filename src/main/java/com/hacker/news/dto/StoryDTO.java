package com.hacker.news.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoryDTO {

    private String title;
    private String url;
    private Integer score;
    @JsonProperty("time")
    private Long timestamp;
    @JsonProperty("by")
    private String submitter;

    @JsonProperty("kids")
    private List<Integer> kids;

    public List<Integer> getKids() {
        return kids;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public int getScore() {
        return score;
    }

    public Instant getTimestamp() {
        return Instant.ofEpochSecond(timestamp);
    }

    public String getSubmitter() {
        return submitter;
    }
}
