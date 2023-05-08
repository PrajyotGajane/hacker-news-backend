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
    private static final String ITEM_URL_TEMPLATE = "https://hacker-news.firebaseio.com/v0/item/%d.json?print=pretty";

    @Cacheable("topStories")
    public List<StoryDTO> getTopTenStoriesByScore() {

        if (storeInDB) {
            newsRepository.truncateTable();
            newsRepository.saveAll(pastStoriesList);
            pastStoriesList.clear();
        }

        ResponseEntity<List> response = restTemplate
                .getForEntity("https://hacker-news.firebaseio.com/v0/topstories.json", List.class);

        List<Integer> list = response.getBody();
        list.forEach(System.out::println);

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

        System.out.println("SIZE OF FINAL LIST : " + storyDTOList.size());

        // Sort stories by score and return the top 10
        List<StoryDTO> topFifList = storyDTOList.stream().sorted(Comparator.comparing(StoryDTO::getScore).reversed()).limit(10).collect(Collectors.toList());

        pastStoriesList = storyMapper.storyDTOListToStoryList(topFifList);

        storeInDB = true;

        return topFifList;
    }

    @Override
    public List<StoryDTO> getPastStories() {

        List<Story> storyList = newsRepository.findAllPastStories();

        List<StoryDTO> storyDTOList = storyMapper.storyListToStoryDTOList(storyList);

        return storyDTOList;
    }

    @Override
    public List<CommentsDTO> getComments(Integer storyId) {

        ResponseEntity<StoryDTO> response = restTemplate
                .getForEntity(String.format("https://hacker-news.firebaseio.com/v0/item/%d.json", storyId), StoryDTO.class);
        StoryDTO storyDTO = response.getBody();

        storyDTO.getKids().forEach(System.out::println);

        storyDTO.getKids().subList(10, storyDTO.getKids().size()).clear();

        List<CompletableFuture<StoryDTO>> futures = new ArrayList<>();
        List<StoryCommentsDTO> storyDTOList = new ArrayList<>();

        for (Integer kidCommentId : storyDTO.getKids()) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                StoryCommentsDTO responseDTO = restTemplate
                        .getForObject(String
                                .format("https://hacker-news.firebaseio.com/v0/item/%d.json", kidCommentId), StoryCommentsDTO.class);
                storyDTOList.add(responseDTO);
                return storyDTO;
            }));
        }


        futures.forEach(CompletableFuture::join);

        storyDTOList.sort(Comparator.comparingInt(comments -> comments.getKids() == null || comments.getKids().isEmpty() ? 0 : comments.getKids().size()));


//        for (StoryCommentsDTO storyCommentsDTO : storyDTOList) {
//            if(!storyCommentsDTO.getKids().isEmpty() && storyCommentsDTO.getKids() != null) {
//                System.out.println("KIDS ARRAY SIZE : " + storyCommentsDTO.getKids().size());
//            }
//        }

        List<CommentsDTO> responseList = storyMapper.storyCommentsListToCommentsDTOList(storyDTOList);

        return responseList;
    }

    private void resetStoreDBFlag() {
        storeInDB = false;
    }

}
