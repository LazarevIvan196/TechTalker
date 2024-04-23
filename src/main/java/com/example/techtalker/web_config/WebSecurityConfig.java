package com.example.techtalker.web_config;

import com.example.techtalker.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SuppressWarnings("deprecation")
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                // Разрешаем доступ к статическим ресурсам и определённым страницам для всех пользователей
                .antMatchers(
                        "/css/**",
                        "/images/**",
                        "/bootstrap/**",
                        "/js/**",
                        "/fonts/**",
                        "/",
                        "/FAQ",
                        "/first_aid",
                        "/registration",
                        "/change-language").permitAll()
                // Доступ только для пользователей с ролью Админ
                .antMatchers("/admin/**", "/topic_edit_form").hasRole("ADMIN")
                // Доступ только для пользователей с ролью USER
                .antMatchers("/new_topic", "/contacts", "current_topic/").hasRole("USER")
                // Все остальные страницы требуют аутентификации
                .anyRequest().authenticated().and()
                // Настройка страницы входа
                .formLogin().loginPage("/login") // Указываем страницу входа
                .defaultSuccessUrl("/") // Перенаправляем на главную страницу после успешного входа
                .permitAll().and()
                // Настройка выхода
                .logout()
                .permitAll() // Разрешаем доступ к операции выхода для всех
                .logoutSuccessUrl("/"); // Указываем страницу после успешного выхода
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

}