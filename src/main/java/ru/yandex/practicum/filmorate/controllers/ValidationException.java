package ru.yandex.practicum.filmorate.controllers;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
