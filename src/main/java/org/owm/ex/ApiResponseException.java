package org.owm.ex;

public class ApiResponseException extends Exception {
    public ApiResponseException(String message) {
        super(message);
    }

    public ApiResponseException(String cod, String message) {
        super(cod + " : " + message);
    }
}
