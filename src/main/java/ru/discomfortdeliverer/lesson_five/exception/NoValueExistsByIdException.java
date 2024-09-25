package ru.discomfortdeliverer.lesson_five.exception;

public class NoValueExistsByIdException extends RuntimeException{
    public NoValueExistsByIdException(String message) {
        super(message);
    }
}
