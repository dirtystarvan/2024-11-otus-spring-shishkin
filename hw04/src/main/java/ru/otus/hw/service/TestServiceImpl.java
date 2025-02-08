package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final AnswerService answerService;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        questions.forEach(question -> {
            var userChoice = ioService.readIntForRangeWithPromptLocalized(1, question.answers().size(),
                    question.toTestString(), "TestService.try.again");
            var isAnswerValid = answerService.isAnswerValid(question, userChoice);
            testResult.applyAnswer(question, isAnswerValid);
        });

        return testResult;
    }

}
