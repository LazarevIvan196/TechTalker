package com.example.techtalker.repositoryes;

import com.example.techtalker.entity.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("SELECT t FROM Topic t WHERE t.title LIKE %:keyword%")
    List<Topic> findByTitleContaining(@Param("keyword") String keyword);
    @Query("SELECT t FROM Topic t ORDER BY t.createdAt DESC")
    List<Topic> findTopNByOrderByCreatedAtDesc(Pageable pageable);
}
