package com.example.n8engine.service.impl;

import com.example.n8engine.service.PhraseService;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class PhraseServiceImpl implements PhraseService {

    public String cleanPhrase(String phrase) {
        String preparedPhrase = "";
        preparedPhrase = phrase.toLowerCase(Locale.ROOT);
        return preparedPhrase;
    }
}
