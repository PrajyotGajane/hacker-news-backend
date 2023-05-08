package com.hacker.news.controller;

import com.hacker.news.dto.CommentsDTO;

import com.hacker.news.dto.StoryDTO;
import com.hacker.news.exception.HackNewsException;
import com.hacker.news.service.IHackerNewsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class HackerNewsController {

    private IHackerNewsService hackerNewsService;

    @GetMapping("/top-stories")
    public List<StoryDTO> getTopTenStoriesByScore() {
        return  hackerNewsService.getTopTenStoriesByScore();
    }

    @GetMapping("/past-stories")
    public List<StoryDTO> getPasStories() {
        return hackerNewsService.getPastStories();
    }

    @GetMapping("/comments/{storyId}")
    public List<CommentsDTO> getComments(@PathVariable("storyId") Integer storyId) {
        //Todo add code to check if id is null and then throw exception
        if(storyId == null) {
            throw new HackNewsException();
        }

        return hackerNewsService.getComments(storyId);
    }

}
