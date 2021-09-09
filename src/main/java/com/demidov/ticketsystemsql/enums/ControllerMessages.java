package com.demidov.ticketsystemsql.enums;

public enum ControllerMessages {

    DELETED("Deleted by id: ");

    private final String value;

    public String getValue() {
        return value;
    }

    ControllerMessages(String value) {
        this.value = value;
    }
}
