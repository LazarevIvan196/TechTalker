package com.example.techtalker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;

    @Size(min = 4, message = "Имя должно содержать минимум 4 знака")
    private String username;
    @Email(regexp=".+@.+\\..+", message = "Электронный адрес должен быть в формате имя@домен.зона")
    private String email;
    @Size(min = 4, message = "Пароль должен содержать минимум 4 знака")
    private String password;
    private String passwordConfirm;


}


