package com.hacker.news.service;

import com.hacker.news.dto.CommentsDTO;
import com.hacker.news.dto.StoryCommentsDTO;
import com.hacker.news.dto.StoryDTO;

import java.util.List;

public interface IHackerNewsService {

    List<StoryDTO> getTopTenStoriesByScore();

    List<StoryDTO> getPastStories();

    List<CommentsDTO> getComments(Integer storyId);
}
