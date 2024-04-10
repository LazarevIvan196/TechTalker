package com.example.techtalker.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String mimeType;
    @Lob
    private byte[] data;
    @Enumerated(EnumType.STRING)
    private AttachmentType type;
    @ManyToOne
    private Topic topic;
    @ManyToOne
    private Message message;
}
