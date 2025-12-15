package jp.ac.chitose.ir.application.exception;

public class APIServerErrorException extends RuntimeException {
    public APIServerErrorException() {}
    public APIServerErrorException(String message) {
        super(message);
    }
}
