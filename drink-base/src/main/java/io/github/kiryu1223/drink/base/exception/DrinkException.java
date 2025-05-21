package io.github.kiryu1223.drink.base.exception;

public class DrinkException extends RuntimeException {
    public DrinkException(String message) {
        super(message);
    }

    public DrinkException() {
    }

    public DrinkException(Throwable cause) {
        super(cause);
    }
}
