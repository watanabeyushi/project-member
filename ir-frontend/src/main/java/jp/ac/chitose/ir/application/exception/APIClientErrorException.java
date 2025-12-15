package jp.ac.chitose.ir.application.exception;

public class APIClientErrorException extends RuntimeException {
    public APIClientErrorException() {}
    public APIClientErrorException(String message) {super(message);}
}
