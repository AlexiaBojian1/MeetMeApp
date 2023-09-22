package com.example.presence.exceptions;

public class NullLocationException extends Exception {
    public NullLocationException(String s) {
        super(s);
    }

    public NullLocationException() {
        super();
    }
}
