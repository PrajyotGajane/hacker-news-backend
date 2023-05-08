package com.hacker.news.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoryCommentsDTO {

    //    3.       /comments - Should return 10 comments (max) on a given story sorted by a total number of child comments.
//    Each comment should contain comment text, the user’s hacker news handle.
    @JsonProperty("by")
    private String by;

    @JsonProperty("id")
    private int id;

    private List<Integer> kids;

    private int parent;
    private String text;

    private int time;

    private String type;

}
