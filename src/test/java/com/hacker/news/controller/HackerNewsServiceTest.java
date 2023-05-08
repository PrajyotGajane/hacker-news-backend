package com.hacker.news.controller;

import com.hacker.news.dto.CommentsDTO;
import com.hacker.news.dto.StoryDTO;

import com.hacker.news.service.impl.HackerNewsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@WebMvcTest(controllers = HackerNewsController.class)
public class HackerNewsServiceTest {

    @MockBean
    private HackerNewsService hackerNewsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnListOfStories() throws Exception {
        StoryDTO storyDTO = new StoryDTO("Google", "http://www.google.com", 2347,
                1683446654293L, "david", Arrays.asList(1, 2, 3, 4, 5));
        StoryDTO storyDTO2 = new StoryDTO("MS", "http://www.microsoft.com", 1234,
                1683446654293L, "goggins", Arrays.asList(1, 2, 3, 4, 5));
        Mockito.when(hackerNewsService.getTopTenStoriesByScore()).thenReturn(Arrays.asList(storyDTO, storyDTO2));

        mockMvc.perform(MockMvcRequestBuilders.get("/top-stories"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    public void shouldReturnListOfPastStories() throws Exception {
        StoryDTO storyDTO = new StoryDTO("Google", "http://www.google.com", 2347,
                1683446654293L, "david", Arrays.asList(1, 2, 3, 4, 5));
        StoryDTO storyDTO2 = new StoryDTO("MS", "http://www.microsoft.com", 1234,
                1683446654293L, "goggins", Arrays.asList(1, 2, 3, 4, 5));
        Mockito.when(hackerNewsService.getPastStories()).thenReturn(Arrays.asList(storyDTO, storyDTO2));

        mockMvc.perform(MockMvcRequestBuilders.get("/past-stories"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldReturnListOfCommentsDTOWhenPassedValidStoryId() throws Exception {
        CommentsDTO commentsDTO_1 = new CommentsDTO("Seen this before", "prajyot");
        CommentsDTO commentsDTO_2 = new CommentsDTO("Seen that before", "david");
        CommentsDTO commentsDTO_3 = new CommentsDTO("Seen where before", "goggins");

        Integer storyId = 8834;

        Mockito.when(hackerNewsService.getComments(storyId)).thenReturn(Arrays.asList(commentsDTO_1, commentsDTO_2, commentsDTO_3));

        mockMvc.perform(MockMvcRequestBuilders.get("/comments/8834"))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }
}
