package com.hacker.news.repository;

import com.hacker.news.model.Story;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<Story, Long> {
    @Query(value = "SELECT * FROM past_stories", nativeQuery = true)
    List<Story> findAllPastStories();

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE past_stories", nativeQuery = true)
    void truncateTable();
}
