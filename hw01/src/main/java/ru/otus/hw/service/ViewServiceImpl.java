package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class ViewServiceImpl implements ViewService {

    private final IOService ioService;

    @Override
    public void showBeginning() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
    }

    @Override
    public void showQuestions(List<Question> questions) {
        questions.forEach(question -> ioService.printFormattedLine(question.toTestString()));
    }
}
