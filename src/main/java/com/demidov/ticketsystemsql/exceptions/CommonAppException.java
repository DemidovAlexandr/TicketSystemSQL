package com.demidov.ticketsystemsql.exceptions;

public class CommonAppException extends RuntimeException {
    String s;

    public CommonAppException(String s) {
        this.s = s;
    }
}
