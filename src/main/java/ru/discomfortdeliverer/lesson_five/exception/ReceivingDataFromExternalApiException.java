package ru.discomfortdeliverer.lesson_five.exception;

public class ReceivingDataFromExternalApiException extends RuntimeException{
    public ReceivingDataFromExternalApiException() {
        super();
    }

    public ReceivingDataFromExternalApiException(String message) {
        super(message);
    }

    public ReceivingDataFromExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReceivingDataFromExternalApiException(Throwable cause) {
        super(cause);
    }
}
