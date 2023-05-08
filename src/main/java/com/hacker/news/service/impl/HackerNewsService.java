package com.hacker.news.service.impl;

import com.hacker.news.dto.CommentsDTO;
import com.hacker.news.dto.StoryCommentsDTO;
import com.hacker.news.dto.StoryDTO;
import com.hacker.news.mapper.IStoryMapper;
import com.hacker.news.model.Story;
import com.hacker.news.repository.NewsRepository;
import com.hacker.news.service.IHackerNewsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class HackerNewsService implements IHackerNewsService {

    private RestTemplate restTemplate;

    private NewsRepository newsRepository;

    private IStoryMapper storyMapper;

    private List<Story> pastStoriesList;

    private static Boolean storeInDB = false;
    private static final String ITEM_URL_TEMPLATE = "https://hacker-news.firebaseio.com/v0/item/%d.json";
    private static final String TOP_STORIES = "https://hacker-news.firebaseio.com/v0/topstories.json";

    @Cacheable("topStories")
    public List<StoryDTO> getTopTenStoriesByScore() {
        log.info("ENTRY : getTopTenStoriesByScore");
        if (storeInDB) {
            newsRepository.truncateTable();
            newsRepository.saveAll(pastStoriesList);
            pastStoriesList.clear();
        }

        ResponseEntity<List> response = restTemplate
                .getForEntity(TOP_STORIES, List.class);

        List<Integer> list = response.getBody();

        List<CompletableFuture<StoryDTO>> futures = new ArrayList<>();
        List<StoryDTO> storyDTOList = new ArrayList<>();

        for (Integer storyId : list) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                StoryDTO storyDTO = restTemplate.getForObject(String.format(ITEM_URL_TEMPLATE, storyId), StoryDTO.class);
                storyDTOList.add(storyDTO);
                return storyDTO;
            }));
        }

        futures.forEach(CompletableFuture::join);

        // Sort stories by score and return the top 10
        List<StoryDTO> topFifList = storyDTOList.stream().sorted(Comparator.comparing(StoryDTO::getScore).reversed()).limit(10).collect(Collectors.toList());

        pastStoriesList = storyMapper.storyDTOListToStoryList(topFifList);

        storeInDB = true;

        log.info("EXIT : getTopTenStoriesByScore");
        return topFifList;
    }

    @Override
    public List<StoryDTO> getPastStories() {
        log.info("ENTRY : getPastStories");
        List<Story> storyList = newsRepository.findAllPastStories();

        List<StoryDTO> storyDTOList = storyMapper.storyListToStoryDTOList(storyList);

        log.info("EXIT : getPastStories");
        return storyDTOList;
    }

    @Override
    public List<CommentsDTO> getComments(Integer storyId) {
        log.info("ENTRY : getComments");
        ResponseEntity<StoryDTO> response = restTemplate
                .getForEntity(String.format(ITEM_URL_TEMPLATE, storyId), StoryDTO.class);
        StoryDTO storyDTO = response.getBody();

        storyDTO.getKids().subList(10, storyDTO.getKids().size()).clear();

        List<CompletableFuture<StoryDTO>> futures = new ArrayList<>();
        List<StoryCommentsDTO> storyDTOList = new ArrayList<>();

        for (Integer kidCommentId : storyDTO.getKids()) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                StoryCommentsDTO responseDTO = restTemplate
                        .getForObject(String
                                .format(ITEM_URL_TEMPLATE, kidCommentId), StoryCommentsDTO.class);
                storyDTOList.add(responseDTO);
                return storyDTO;
            }));
        }

        futures.forEach(CompletableFuture::join);

        storyDTOList.sort(Comparator.comparingInt(comments -> comments.getKids() == null || comments.getKids().isEmpty() ? 0 : comments.getKids().size()));

        List<CommentsDTO> responseList = storyMapper.storyCommentsListToCommentsDTOList(storyDTOList);

        log.info("ENTRY : getComments");
        return responseList;
    }

}
