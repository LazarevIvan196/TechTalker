package com.example.techtalker.services;

import com.example.techtalker.dto.TopicDto;
import com.example.techtalker.entity.Topic;
import com.example.techtalker.entity.TopicStatus;
import com.example.techtalker.entity.User;
import com.example.techtalker.repositoryes.TopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@SuppressWarnings("ALL")
@AllArgsConstructor
@Service
public class TopicService {
    private final TopicRepository topicRepository;
    private final UserService userService;

    @Transactional
    public TopicDto save(TopicDto topicDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = (User) userService.loadUserByUsername(currentUserName);
        Topic topic = convertToEntity(topicDto);
        topic.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        topic.setStatus(TopicStatus.OPEN);
        topic.setCreator(currentUser);
        Topic savedTopic = topicRepository.save(topic);
        return convertToDto(savedTopic);
    }


    public List<TopicDto> findAll() {
        return topicRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteByTopicId(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            topicRepository.deleteById(id);
        } else {

            throw new AccessDeniedException("Нет доступа для выполнения операции");
        }
    }

    public List<TopicDto> findByTitleContaining(String keyword) {
        return topicRepository.findByTitleContaining(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<TopicDto> findByTopicId(Long topicId) {
        return topicRepository.findById(topicId).map(this::convertToDto);
    }
    public Optional<Topic> findTopicById(Long topicId) {
        return topicRepository.findById(topicId);
    }

    public List<TopicDto> findLatestTopics() {
        Pageable pageable = PageRequest.of(0, 7, Sort.by("createdAt").descending());

        return topicRepository.findTopNByOrderByCreatedAtDesc(pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public void changeStatus(Long id, String status) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Тема не найдена"));
        topic.setStatus(TopicStatus.valueOf(status));
        topicRepository.save(topic);
    }

    @Transactional
    public void updateTopic(Long topicId, TopicDto topicDto) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Тема с id=" + topicId + " не найдена."));
        topic.setTitle(topicDto.getTitle());
        topic.setContent(topicDto.getContent());
        topicRepository.save(topic);
    }

    private TopicDto convertToDto(Topic topic) {
        TopicDto dto = new TopicDto();
        dto.setId(topic.getId());
        dto.setTitle(topic.getTitle());
        dto.setContent(topic.getContent());
        dto.setStatus(topic.getStatus());
        dto.setCreatedAt(topic.getCreatedAt());
        if (topic.getCreator() != null) {
            dto.setCreatorName(topic.getCreator().getUsername());
        }
        return dto;
    }

    private Topic convertToEntity(TopicDto topicDto) {
        Topic topic = new Topic();
        topic.setId(topicDto.getId());
        topic.setTitle(topicDto.getTitle());
        topic.setContent(topicDto.getContent());
        return topic;
    }

}