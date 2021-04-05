package com.example.n8engine.service.impl;

import com.example.n8engine.service.PhraseService;

import java.util.Locale;

public class PhraseServiceImpl implements PhraseService {

    public String cleanPhrase(String phrase) {
        String preparedPhrase = "";
        preparedPhrase = phrase.toLowerCase(Locale.ROOT);
        return preparedPhrase;
    }
}
