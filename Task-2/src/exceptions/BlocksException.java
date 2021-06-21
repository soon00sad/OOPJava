package com.company.exceptions;

public class BlocksException extends WorkflowException {
    public BlocksException(String message) {
        super(message);
    }
    public BlocksException(String message, Throwable cause) {
        super(message, cause);
    }
    public BlocksException(Throwable cause) {
        super(cause);
    }
    public BlocksException() {
    }
}
