package com.example.jdnc_library.domain.book.model;

public enum BookGroup {

    GROUP_T("T"),
    GROUP_A("A"),
    GROUP_M("M"),
    GROUP_N("N"),
    GROUP_a("a");

    private final String value;

    BookGroup(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}