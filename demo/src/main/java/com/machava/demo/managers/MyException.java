package com.machava.demo.managers;

public class MyException extends Exception {
    MyException(String m) {
        super(m, null, true, false);
    }

    @Override
    public String toString() {
        return getLocalizedMessage();
    }
}
