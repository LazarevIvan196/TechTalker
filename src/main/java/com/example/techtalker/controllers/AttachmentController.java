package com.example.techtalker.controllers;
import com.example.techtalker.services.AttachmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class AttachmentController {
    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/messages/{messageId}/attachments")
    public String addAttachmentToMessage(@PathVariable Long messageId,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        attachmentService.saveAttachment(file, messageId, "message");
        return "redirect:/messages/" + messageId;
    }

    // Добавление вложения к теме
    @PostMapping("/topics/{topicId}/attachments")
    public String addAttachmentToTopic(@PathVariable Long topicId,
                                       @RequestParam("file") MultipartFile file) throws IOException {
        attachmentService.saveAttachment(file, topicId, "topic");
        return "redirect:/topics/" + topicId;
    }

}