package com.example.techtalker.services;

import com.example.techtalker.dto.MessageDto;
import com.example.techtalker.entity.Message;
import com.example.techtalker.entity.Topic;
import com.example.techtalker.entity.User;
import com.example.techtalker.repositoryes.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final TopicService topicService;


    @Transactional
    public MessageDto save(MessageDto messageDto) {
        Message message = new Message();
        message.setContent(messageDto.getContent());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User author = userService.findUserByName(currentPrincipalName);
        Optional<Topic> topicOptional = topicService.findTopicById(messageDto.getTopicId());
        Topic topicEntity = topicOptional.orElseThrow(() -> new EntityNotFoundException("Тема не найдена"));
        message.setAuthor(author);
        message.setTopic(topicEntity);
        message.setCreatedAt(LocalDateTime.now());
        message = messageRepository.save(message);

        return convertToDto(message);
    }

    @Transactional
    public MessageDto update(MessageDto messageDto) {
        Message message = messageRepository.findById(messageDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Сообщение с ID " + messageDto.getId() + " не найдено"));

        message.setContent(messageDto.getContent());

        Message updatedMessage = messageRepository.save(message);
        return convertToDto(updatedMessage);
    }

    public Optional<MessageDto> findById(Long id) {
        return messageRepository.findById(id).map(this::convertToDto);
    }

    public List<MessageDto> findByTopicId(Long topicId) {
        return messageRepository.findByTopicId(topicId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Сообщение с ID " + messageId + " не найдено"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        boolean isAuthor = message.getAuthor().getUsername().equals(currentUser.getUsername());
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAuthor || isAdmin) {
            messageRepository.deleteById(messageId);
        } else {
            throw new AccessDeniedException("Нет доступа для выполнения операции");
        }
    }

    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setTopicId(message.getTopic().getId());
        dto.setUser(message.getAuthor());
        dto.setContent(message.getContent());
        dto.setCreatedAt(message.getCreatedAt());

        return dto;
    }
}
