package ru.itis.flavorful_book.exception;

import lombok.Getter;

@Getter
public class IllegalUserArgumentException extends RuntimeException {
    private String usernameState;

    private String emailState;

    private boolean shouldThrow;

    public IllegalUserArgumentException(String message) {
        super(message);

        usernameState = null;
        emailState = null;

        shouldThrow = false;
    }

    public void setUsernameState(String usernameState) {
        if (usernameState != null) {
            this.usernameState = usernameState;
            shouldThrow = true;
        }
    }

    public void setEmailState(String emailState) {
        if (emailState != null) {
            this.emailState = emailState;
            shouldThrow = true;
        }
    }
}
