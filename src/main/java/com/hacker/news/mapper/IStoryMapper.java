package com.hacker.news.mapper;

import com.hacker.news.dto.CommentsDTO;
import com.hacker.news.dto.StoryCommentsDTO;
import com.hacker.news.dto.StoryDTO;
import com.hacker.news.model.Story;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IStoryMapper {

    Story storyDtoToStory(StoryDTO storyDTO);

    List<Story> storyDTOListToStoryList(List<StoryDTO> storyDTOList);

    List<StoryDTO> storyListToStoryDTOList(List<Story> storyList);

    StoryDTO storyDtoToStory(Story story);

    CommentsDTO storyCommentsDTOToCommentsDTO(StoryCommentsDTO storyCommentsDTO);

    List<CommentsDTO> storyCommentsListToCommentsDTOList(List<StoryCommentsDTO> storyCommentsDTOS);

}
