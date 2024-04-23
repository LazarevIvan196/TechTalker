package com.example.techtalker.controllers;

import com.example.techtalker.dto.MessageDto;
import com.example.techtalker.dto.TopicDto;
import com.example.techtalker.services.AttachmentService;
import com.example.techtalker.services.MessageService;
import com.example.techtalker.services.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@AllArgsConstructor
@Controller
public class TopicController {

    private final TopicService topicService;
    private final AttachmentService attachmentService;
    private final MessageService messageService;

    @GetMapping("/new_topic")
    public String newTopicForm(Model model) {
        model.addAttribute("topic", new TopicDto());
        loadLatestTopics(model);
        return "new_topic";
    }

    @GetMapping("/")
    public String index(Model model) {
        List<TopicDto> latestTopics = topicService.findLatestTopics();
        model.addAttribute("topics", latestTopics);

        return "index";
    }

    @PostMapping("/topics/add")
    public String addTopic(@ModelAttribute("topic") @Valid TopicDto topicDto,
                           BindingResult result,
                           @RequestParam(value = "files", required = false) MultipartFile[] files,
                           RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            return "new_topic";
        }

        TopicDto savedTopicDto = topicService.save(topicDto);
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    attachmentService.saveAttachment(file, savedTopicDto.getId(), "topic");
                }
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Тема успешно создана!");
        return "redirect:/";
    }

    @PostMapping("/topics/{topicId}/delete")
    public String deleteTopic(@PathVariable Long topicId, RedirectAttributes redirectAttributes) {
        topicService.deleteByTopicId(topicId);
        redirectAttributes.addFlashAttribute("successMessage", "Тема успешно удалена!");
        return "redirect:/";
    }

    private void loadLatestTopics(Model model) {
        List<TopicDto> latestTopics = topicService.findLatestTopics();
        model.addAttribute("topics", latestTopics);
    }

    @GetMapping("/current_topic/{topicId}")
    public String currentTopic(@PathVariable Long topicId, Model model) {
        TopicDto topicDto = topicService.findByTopicId(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Топик с id:" + topicId + " не найден"));
        model.addAttribute("topic", topicDto);
        List<MessageDto> messages = messageService.findByTopicId(topicId);
        model.addAttribute("messages", messages);
        model.addAttribute("newMessage", new MessageDto());

        return "current_topic";
    }

    @PostMapping("/topics/{topicId}/messages/add")
    public String addMessage(@PathVariable Long topicId,
                             @ModelAttribute("newMessage") MessageDto messageDto,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             RedirectAttributes redirectAttributes) throws IOException {
        messageDto.setTopicId(topicId);
        MessageDto savedMessage = messageService.save(messageDto);
        if (file != null && !file.isEmpty()) {
            attachmentService.saveAttachment(file, savedMessage.getId(), "message");
        }

        redirectAttributes.addFlashAttribute("successMessage", "Сообщение успешно добавлено.");
        return "redirect:/current_topic/" + topicId;
    }
}

