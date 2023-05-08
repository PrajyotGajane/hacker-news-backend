package com.hacker.news.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "past_stories")
@Data
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "story_id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;
    @Column(name = "story_url")
    private String url;

    @Column(name = "score")
    private Integer score;

    @Column(name = "time_stamp")
    private Instant timestamp;

    @Column(name = "submitter")
    private String submitter;

    public Long getTimestamp() {
        return timestamp.getEpochSecond();
    }
}
