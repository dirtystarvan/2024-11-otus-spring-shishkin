package ru.otus.hw.service;

import java.io.InputStreamReader;

public interface ResourceUtility {
    InputStreamReader getResourceReader(String resourcePath);
}
