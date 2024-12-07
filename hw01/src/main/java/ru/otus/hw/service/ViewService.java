package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

import java.util.List;

public interface ViewService {
    void showBeginning();

    void showQuestions(List<Question> questions);
}
