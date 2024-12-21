package ru.otus.hw.service;

import ru.otus.hw.exceptions.QuestionsResourceException;

import java.io.InputStreamReader;
import java.util.Optional;

public class ClassPathResourceUtility implements ResourceUtility {

    private static final String RESOURCE_NOT_FOUND = "Resource not found";

    @Override
    public InputStreamReader getResourceReader(String resourcePath) {
        var inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);

        return Optional.ofNullable(inputStream)
                .map(InputStreamReader::new)
                .orElseThrow(() -> new QuestionsResourceException(RESOURCE_NOT_FOUND));
    }
}
