package com.example.techtalker.dto;

import com.example.techtalker.entity.AttachmentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttachmentDto {
    private Long id;
    private AttachmentType type;
    private String fileName;
    private String mimeType;
    private Long messageId;
    private Long topicId;

}
