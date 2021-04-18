package ru.javawebinar.topjava.util.exception;

public enum ErrorType {
    APP_ERROR ("Application error."),
    DATA_NOT_FOUND ("Data not found error."),
    DATA_ERROR ("Data error."),
    VALIDATION_ERROR ("Data validation error.");

    String description;

    ErrorType(String description) {
        this.description = description;
    }
}