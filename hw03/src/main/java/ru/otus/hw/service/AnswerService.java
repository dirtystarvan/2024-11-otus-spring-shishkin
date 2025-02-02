package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface AnswerService {

    boolean isAnswerValid(Question question, int userAnswer);

}
