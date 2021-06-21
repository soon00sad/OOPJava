package com.company.exceptions;

public class DescriptionException extends  WorkflowException {
    public DescriptionException(String message) {
        super(message);
    }
    public DescriptionException(String message, Throwable cause) {
        super(message, cause);
    }
    public DescriptionException(Throwable cause) {
        super(cause);
    }
    public DescriptionException() {
    }
}