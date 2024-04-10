package com.example.techtalker.dto;
import com.example.techtalker.entity.TopicStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TopicDto {
    private Long id;
    @Size(min = 10, message = "Заголовок темы должен  содержать минимум 10 знаков")
    private String title;
    @Size(min = 15, message = "Описание проблемы должно  содержать минимум 15 знаков")
    private String content;
    private TopicStatus status;
    private Long creatorId;
    private LocalDateTime createdAt;
    private String creatorName;
    private List<MessageDto> messages;
    private List<AttachmentDto> attachments;
}