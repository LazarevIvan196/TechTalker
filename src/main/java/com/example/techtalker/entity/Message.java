package com.example.techtalker.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private Topic topic;
    @ManyToOne
    private User author;
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)

    private Set<Attachment> attachments;
    private LocalDateTime createdAt;
}
