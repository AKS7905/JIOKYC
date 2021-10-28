package com.example.jiokyc.api;

/**
 * Created by ankit on 8/31/2017.
 */

public class RetroError {
    private final int httpErrorCode;
    private final Kind kind;
    private String errorMessage;

    public RetroError(Kind kind, String errorMessage, int httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
        this.kind = kind;
        this.errorMessage = errorMessage;
    }

    public int getHttpErrorCode() {
        return this.httpErrorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Kind getKind() {
        return this.kind;
    }

    public enum Kind {
        NETWORK,
        HTTP,
        UNEXPECTED;

        Kind() {
        }
    }
}
