package ru.discomfortdeliverer.lesson_five.exception;

public class ValueByIdAlreadyExistsException extends RuntimeException{
    public ValueByIdAlreadyExistsException(String message) {
        super(message);
    }
}
