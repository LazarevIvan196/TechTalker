package com.example.techtalker.repositoryes;

import com.example.techtalker.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findById(Long id);
    List<Message> findByTopicId(Long topicId);
}