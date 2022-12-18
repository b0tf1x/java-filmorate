package ru.yandex.practicum.filmorate.controllers;

import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.exceptions.UserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidationException(final ValidationException exception) {
        log.warn("Ошибка проверки данных {}" + exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerUserException(final UserException exception) {
        log.warn("Ошибка при работе с пользователем {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerFilmException(final FilmException exception) {
        log.warn("Ошибка при работе с фильмами {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerInternalException(final InternalException exception) {
        log.warn("Ошибка сервера {}", exception.getMessage());
        return new ErrorResponse("Internal error 500 " + exception.getMessage());
    }
}
