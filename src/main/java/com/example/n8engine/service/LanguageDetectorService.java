package com.example.n8engine.service;

import opennlp.tools.langdetect.Language;

public interface LanguageDetectorService {
    Language detectLanguage(String searchPhrase);
    Language[] detectLanguages(String searchPhrase);
}
