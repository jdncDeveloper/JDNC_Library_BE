package com.example.jdnc_library.domain.book.model;

public enum BookGroup {

    GROUP_T("T"),
    GROUP_A("A"),
    GROUP_M("M"),
    GROUP_N("N"),
    GROUP_A2("A2");

    private final String value;

    BookGroup(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BookGroup fromValue(String value) {
        for (BookGroup group : values()) {
            if (group.getValue().equals(value)) {
                return group;
            }
        }
        throw new IllegalArgumentException("Unknown BookGroup value: " + value);
    }
}