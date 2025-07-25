package ru.netology.springBootDemo.exception;

public class TransferProcessingException extends RuntimeException{
    public TransferProcessingException(String message) {
        super(message);
    }
}
