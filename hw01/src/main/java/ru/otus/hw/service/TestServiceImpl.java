package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final QuestionDao questionDao;

    private final ViewService viewService;

    @Override
    public void executeTest() {
        viewService.showBeginning();

        var questions = questionDao.findAll();

        viewService.showQuestions(questions);
    }
}
