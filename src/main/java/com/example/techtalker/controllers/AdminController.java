package com.example.techtalker.controllers;

import com.example.techtalker.dto.MessageDto;
import com.example.techtalker.dto.TopicDto;
import com.example.techtalker.services.MessageService;
import com.example.techtalker.services.TopicService;
import com.example.techtalker.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final TopicService topicService;
    private final MessageService messageService;

    // Отображение списка пользователей и тем для администрирования
    @GetMapping
    public String showAdminDashboard(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("topics", topicService.findAll());
        return "admin";
    }

    // Удаление пользователя
    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin";
    }

    // Удаление темы
    @GetMapping("/topics/delete/{id}")
    public String deleteTopic(@PathVariable Long id) {
        topicService.deleteByTopicId(id);
        return "redirect:/admin";
    }

    // Отображение формы для редактирования темы
    @GetMapping("/topics/edit/{id}")
    public String showEditTopicForm(@PathVariable Long id, Model model) {
        TopicDto topicDto = topicService.findByTopicId(id)
                .orElseThrow(() -> new EntityNotFoundException("Тема с ID " + id + " не найдена."));
        List<MessageDto> messages = messageService.findByTopicId(id);
        model.addAttribute("topic", topicDto);
        model.addAttribute("messages", messages);
        return "topic_edit_form";
    }

    // Обновление темы
    @PostMapping("/topics/update/{id}")
    public String updateTopic(@PathVariable Long id, @ModelAttribute("topic") TopicDto topicDto, RedirectAttributes redirectAttributes) {
        topicService.updateTopic(id, topicDto);
        redirectAttributes.addFlashAttribute("successMessage", "Тема успешно обновлена!");
        return "redirect:/admin/topics/edit/" + id;
    }

    // Открытие темы
    @PostMapping("/topics/open/{id}")
    public String openTopic(@PathVariable Long id) {
        topicService.changeStatus(id, "OPEN");
        return "redirect:/admin";
    }

    // Закрытие темы
    @PostMapping("/topics/close/{id}")
    public String closeTopic(@PathVariable Long id) {
        topicService.changeStatus(id, "CLOSED");
        return "redirect:/admin";
    }
}

