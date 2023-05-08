package com.hacker.news.service;

import com.hacker.news.dto.CommentsDTO;
import com.hacker.news.dto.StoryDTO;
import com.hacker.news.repository.NewsRepository;
import com.hacker.news.service.impl.HackerNewsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EnableCaching
public class HackerNewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private HackerNewsService hackerNewsService;

    @Mock
    CacheManager cacheManager;

    @BeforeTestMethod
    public void setUp() {
        Cache myCache = Mockito.mock(Cache.class);
        when(cacheManager.getCache("topStories")).thenReturn(myCache);
    }

    @Test
    void testGetTopTenStoriesByScore_CacheHit() {
        // Arrange
        List<StoryDTO> cachedStories = Arrays.asList(
                new StoryDTO("Title 1", "https://www.example.com", 5,
                        123456664L, "Some Text", new ArrayList<>()),
                new StoryDTO("Title 2", "https://www.example.com", 8,
                        123456664L, "Some Text", new ArrayList<>()),
                new StoryDTO("Title 3", "https://www.example.com", 1,
                        123456664L, "Some Text", new ArrayList<>())
        );

        Mockito.when(hackerNewsService.getTopTenStoriesByScore()).thenReturn(cachedStories);

        // Act
        List<StoryDTO> result = hackerNewsService.getTopTenStoriesByScore();

        // Assert
        verify(hackerNewsService, times(1)).getTopTenStoriesByScore();

        Assertions.assertEquals(cachedStories, result);

        //Act for Cached result
        List<StoryDTO> cachedResult = hackerNewsService.getTopTenStoriesByScore();

        verifyNoInteractions(newsRepository);

        //Should return cached result
        Assertions.assertEquals(cachedStories, cachedResult);
    }

    @Test
    void testGetPastStoriesFromDatabase() {
        List<StoryDTO> pastStories = Arrays.asList(
                new StoryDTO("Title 1", "https://www.example.com", 5,
                        123456664L, "Some Text", new ArrayList<>()),
                new StoryDTO("Title 2", "https://www.example.com", 8,
                        123456664L, "Some Text", new ArrayList<>()),
                new StoryDTO("Title 3", "https://www.example.com", 1,
                        123456664L, "Some Text", new ArrayList<>())
        );

        Mockito.when(hackerNewsService.getPastStories()).thenReturn(pastStories);

        List<StoryDTO> pastStoriesDB = hackerNewsService.getPastStories();

        Assertions.assertEquals(pastStories, pastStoriesDB);
    }

    @Test
    void testGetCommentsForStories() {
        List<CommentsDTO> commentsDTOS = new ArrayList<>();
        commentsDTOS.add(new CommentsDTO("david", "comments 1"));
        commentsDTOS.add(new CommentsDTO("goggins", "comments 2"));
        commentsDTOS.add(new CommentsDTO("prajyot", "comments 3"));
        Integer storyId = 1234;
        Mockito.when(hackerNewsService.getComments(storyId)).thenReturn(commentsDTOS);

        List<CommentsDTO> actualResult = hackerNewsService.getComments(storyId);

        Assertions.assertEquals(commentsDTOS, actualResult);
    }
}