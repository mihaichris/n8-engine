package com.example.n8engine.enumeration;

import java.util.Locale;

public enum Language {
    ENGLISH;

    public String getShortCode() {
        return this.toString().toLowerCase(Locale.ROOT).substring(0, 2);
    }
}
