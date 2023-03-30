package com.example.JWT.exception;
import lombok.Getter;

@Getter
public enum ResponseErrors {
    BAD_REQUEST(400, "BAD_REQUEST"),
    UNAUTHENTICATED(401, "UNAUTHENTICATED"),
    CONFLICT(409, "CONFLICT");

    private int code;
    private String type;

    ResponseErrors(int code, String type) {
        this.code = code;
        this.type = type;
    }
}
