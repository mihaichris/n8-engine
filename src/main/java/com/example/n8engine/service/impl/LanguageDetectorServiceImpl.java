package com.example.n8engine.service.impl;

import com.example.n8engine.service.LanguageDetectorService;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class LanguageDetectorServiceImpl implements LanguageDetectorService {

    private LanguageDetector categorizer;

    public LanguageDetectorServiceImpl(Environment environment) {
        try {
            String modelPath = environment.getProperty("opennlp.model.languages.detector.path");
            try(InputStream is = new FileInputStream(modelPath)) {
                LanguageDetectorModel m = new LanguageDetectorModel(is);
                this.categorizer = new LanguageDetectorME(m);
            }
        } catch (IOException exception) {
            log.error("Error:" + exception.getMessage());
        }
    }

    public Language detectLanguage(String searchPhrase)
    {
        return this.categorizer.predictLanguage(searchPhrase);
    }
}
