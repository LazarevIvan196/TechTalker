package com.example.techtalker.controllers;

import com.example.techtalker.dto.UserDto;
import com.example.techtalker.exception.DuplicateUserException;
import com.example.techtalker.services.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
@AllArgsConstructor
@Controller
public class RegistrationController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDto());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid UserDto userDto, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        try {
            userService.saveUser(userDto);
            redirectAttributes.addFlashAttribute("successMessage", "Регистрация прошла успешно! <br/> Вы можете войти в систему.");
            return "redirect:/";
        } catch (DuplicateUserException ex) {
            redirectAttributes.addFlashAttribute("usernameError", "Пользователь с таким именем или почтой уже существует!");
            return "redirect:/registration";
        } catch (Exception ex) {
            logger.error("Ошибка при регистрации: ", ex);
            redirectAttributes.addFlashAttribute("registrationError", "Произошла ошибка при регистрации. Пожалуйста, попробуйте еще раз.");
            return "redirect:/registration";
        }
    }
}