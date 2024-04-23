package com.example.techtalker.test_controllers;

import com.example.techtalker.controllers.TopicController;
import com.example.techtalker.dto.AttachmentDto;
import com.example.techtalker.dto.MessageDto;
import com.example.techtalker.dto.TopicDto;
import com.example.techtalker.entity.AttachmentType;
import com.example.techtalker.services.AttachmentService;
import com.example.techtalker.services.MessageService;
import com.example.techtalker.services.TopicService;
import com.example.techtalker.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TopicController.class)
@WithMockUser(roles = "USER")
public class TopicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService topicService;

    @MockBean
    private AttachmentService attachmentService;

    @MockBean
    private MessageService messageService;

    @SuppressWarnings("unused")
    @MockBean
    private UserService userService;

    @Test
    public void addTopic_whenValidInput_thenRedirectsToIndex() throws Exception {
        TopicDto topicDto = new TopicDto();
        topicDto.setTitle("Тестовая тема");
        topicDto.setContent("Это тестовая тема.");

        when(topicService.save(any(TopicDto.class))).thenReturn(topicDto);

        MockMultipartFile file = new MockMultipartFile("files", "test.txt",
                "text/plain", "Тестовое содержимое".getBytes());

        mockMvc.perform(multipart("/topics/add")
                        .file(file)
                        .param("title", "Тестовая тема")
                        .param("content", "Это тестовая тема.")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void addTopic_withAttachments_thenAttachmentsAreSaved() throws Exception {
        TopicDto topicDto = new TopicDto();
        topicDto.setId(1L);
        topicDto.setTitle("Тестовая тема с вложениями");
        topicDto.setContent("Это тестовая тема с вложениями.");

        when(topicService.save(any(TopicDto.class))).thenReturn(topicDto);

        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(1L);
        attachmentDto.setFileName("test.txt");
        attachmentDto.setMimeType("text/plain");
        attachmentDto.setType(AttachmentType.FILE);
        attachmentDto.setTopicId(topicDto.getId());

        when(attachmentService.saveAttachment(any(MultipartFile.class), eq(topicDto.getId()), eq("topic")))
                .thenReturn(attachmentDto);

        MockMultipartFile file = new MockMultipartFile("files", "test.txt", "text/plain", "Тестовое содержимое".getBytes());

        mockMvc.perform(multipart("/topics/add")
                        .file(file)
                        .param("title", topicDto.getTitle())
                        .param("content", topicDto.getContent())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(attachmentService, times(1)).saveAttachment(any(MultipartFile.class), eq(topicDto.getId()), eq("topic"));
    }

    @Test
    public void addMessageToTopic_whenValidInput_thenRedirectsToTopic() throws Exception {

        Long topicId = 1L;

        MessageDto messageDto = new MessageDto();
        messageDto.setContent("Это тестовое сообщение.");
        messageDto.setTopicId(topicId);

        when(messageService.save(any(MessageDto.class))).thenReturn(messageDto);

        mockMvc.perform(multipart("/topics/{topicId}/messages/add", topicId)
                        .param("content", messageDto.getContent())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/current_topic/" + topicId));

        verify(messageService, times(1)).save(any(MessageDto.class));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }


}

