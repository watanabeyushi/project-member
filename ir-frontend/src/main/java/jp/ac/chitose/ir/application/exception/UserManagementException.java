package jp.ac.chitose.ir.application.exception;

public class UserManagementException  extends RuntimeException{
    public UserManagementException() {}
    public UserManagementException(String message) {
        super(message);
    }
}
