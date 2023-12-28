package ru.khusnullin.SpringBootUpdate2.utils;

public class UserNotValidException extends RuntimeException{
    public UserNotValidException(String message) {
        super(message);
    }
}
