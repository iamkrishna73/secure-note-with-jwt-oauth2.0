package io.iamkrishna73.notes.exception;

import lombok.Getter;

@Getter
public class RoleNotFoundException extends RuntimeException{
    private String errorCode;

    public RoleNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
