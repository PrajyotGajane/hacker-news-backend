package com.hacker.news.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "PAST_STORIES")
@Data
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STORY_ID", nullable = false)
    private Long id;

    @Column(name = "TITLE")
    private String title;
    @Column(name = "STORY_URL")
    private String url;

    @Column(name = "SCORE")
    private Integer score;

    @Column(name = "TIME_STAMP")
    private Instant timestamp;

    @Column(name = "SUBMITTER")
    private String submitter;

    public Long getTimestamp() {
        return timestamp.getEpochSecond();
    }
}
