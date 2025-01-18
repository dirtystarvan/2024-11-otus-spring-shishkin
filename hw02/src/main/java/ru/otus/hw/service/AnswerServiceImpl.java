package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Question;

import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Override
    public boolean isAnswerValid(Question question, int userAnswer) {
        var answers = Optional.ofNullable(question).map(Question::answers).orElse(null);

        if (answers == null || answers.size() < userAnswer) {
            return false;
        }

        return answers.get(userAnswer - 1).isCorrect();
    }

}
