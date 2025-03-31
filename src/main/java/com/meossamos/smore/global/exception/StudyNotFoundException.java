package com.meossamos.smore.global.exception;

public class StudyNotFoundException extends RuntimeException {
    public StudyNotFoundException(String message) {
        super(message);
    }
}