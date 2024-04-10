package com.example.techtalker.controllers;

import com.example.techtalker.dto.MessageDto;
import com.example.techtalker.services.MessageService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;

@AllArgsConstructor
@Controller
public class MessageController {

    private final MessageService messageService;

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @PostMapping("/topics/{topicId}/messages")
    public String addMessage(@PathVariable Long topicId, @ModelAttribute("message") MessageDto messageDto) {
        logger.info("Добавление сообщения в тему с ID: {}", topicId);
        messageDto.setTopicId(topicId);
        messageService.save(messageDto);
        return "redirect:/topics/" + topicId;
    }

    @GetMapping("/messages/edit/{topicId}/{id}")
    public String editMessageForm(@PathVariable Long topicId, @PathVariable Long id, Model model) {
        logger.info("Редактирование сообщения с ID: {}", id);
        MessageDto messageDto = messageService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invalid message Id:" + id));
        model.addAttribute("message", messageDto);
        model.addAttribute("topicId", topicId);
        return "message_edit_form";
    }

    @PostMapping("/messages/delete/{id}/{topicId}")
    public String deleteMessage(@PathVariable Long id, @PathVariable Long topicId, RedirectAttributes redirectAttributes) {
        logger.info("Удаление сообщения с ID: {}", id);
         messageService.deleteMessage(id);
        redirectAttributes.addFlashAttribute("successMessage", "Сообщение удалено.");
        return "redirect:/admin/topics/edit/" + topicId;
    }

    @PostMapping("/messages/update/{topicId}/{id}")
    public String updateMessage(@PathVariable Long topicId, @PathVariable Long id, @ModelAttribute("message") MessageDto messageDto,
                                RedirectAttributes redirectAttributes) {
        logger.info("Обновление сообщения с ID: {}", id);
        messageDto.setId(id);
        messageService.update(messageDto);
        redirectAttributes.addFlashAttribute("successMessage", "Сообщение успешно обновлено.");
        return "redirect:/admin/topics/edit/" + topicId;
    }
}
