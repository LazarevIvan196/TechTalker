package com.example.techtalker.dto;

import com.example.techtalker.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private Long topicId;
    private User user;
    @Size(min = 1, message = "Сообщение не может быть пустым!")
    private String content;
    private List<AttachmentDto> attachments;
    private LocalDateTime createdAt;
}
