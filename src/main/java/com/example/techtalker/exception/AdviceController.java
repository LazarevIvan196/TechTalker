package com.example.techtalker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException exception) {
        return new ResponseEntity<>("Доступ запрещен! " + exception.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return new ResponseEntity<>("Пользователь не найден! " + exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUserException(DuplicateUserException exception) {
        return new ResponseEntity<>("Ошибка регистрации! " + exception.getMessage(), HttpStatus.CONFLICT);
    }

    // Общий обработчик для прочих исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception exception) {
        return new ResponseEntity<>("Произошла ошибка! " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
