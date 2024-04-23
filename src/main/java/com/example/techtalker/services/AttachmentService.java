package com.example.techtalker.services;

import com.example.techtalker.dto.AttachmentDto;
import com.example.techtalker.entity.Attachment;
import com.example.techtalker.entity.AttachmentType;
import com.example.techtalker.entity.Message;
import com.example.techtalker.entity.Topic;
import com.example.techtalker.repositoryes.AttachmentRepository;
import com.example.techtalker.repositoryes.MessageRepository;
import com.example.techtalker.repositoryes.TopicRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;
@SuppressWarnings("ALL")
@AllArgsConstructor
@Service
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final MessageRepository messageRepository;
    private final TopicRepository topicRepository;


    @Transactional
    public AttachmentDto saveAttachment(MultipartFile file, Long entityId, String entityType) throws IOException {
        Attachment attachment = new Attachment();

        if ("message".equalsIgnoreCase(entityType)) {
            Message message = messageRepository.findById(entityId)
                    .orElseThrow(() -> new EntityNotFoundException("Сообщение с ID " + entityId + " не найдено"));
            attachment.setMessage(message);
        } else if ("topic".equalsIgnoreCase(entityType)) {
            Topic topic = topicRepository.findById(entityId)
                    .orElseThrow(() -> new EntityNotFoundException("Тема с ID " + entityId + " не найдена"));
            attachment.setTopic(topic);
        } else {
            throw new IllegalArgumentException("Неверный тип сущности для вложения");
        }
        attachment.setFileName(file.getOriginalFilename());
        attachment.setMimeType(file.getContentType());
        attachment.setData(file.getBytes());
        attachment.setType(determineAttachmentType(file.getContentType()));
        attachment = attachmentRepository.save(attachment);

        return convertToDto(attachment);
    }

    public Optional<AttachmentDto> findAttachmentById(Long id) {
        return attachmentRepository.findById(id)
                .map(this::convertToDto);
    }

    private AttachmentType determineAttachmentType(String mimeType) {
        if (mimeType.startsWith("image/")) {
            return AttachmentType.IMAGE;
        } else {
            return AttachmentType.FILE;
        }
    }

    @Transactional
    public void deleteByAttachmentId(Long id) {
        attachmentRepository.deleteById(id);
    }

    protected AttachmentDto convertToDto(Attachment attachment) {
        AttachmentDto dto = new AttachmentDto();
        dto.setId(attachment.getId());
        dto.setType(attachment.getType());
        dto.setFileName(attachment.getFileName());
        dto.setMimeType(attachment.getMimeType());

        if (attachment.getMessage() != null) {
            dto.setMessageId(attachment.getMessage().getId());
        }
        if (attachment.getTopic() != null) {
            dto.setTopicId(attachment.getTopic().getId());
        }

        return dto;
    }
}