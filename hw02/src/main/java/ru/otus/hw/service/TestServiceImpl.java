package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String TRY_AGAIN_MESSAGE = "Try again%n";

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final AnswerService answerService;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        questions.forEach(question -> {
            var userChoice = ioService.readIntForRangeWithPrompt(1, question.answers().size(),
                    question.toTestString(), TRY_AGAIN_MESSAGE);
            var isAnswerValid = answerService.isAnswerValid(question, userChoice);
            testResult.applyAnswer(question, isAnswerValid);
        });

        return testResult;
    }

}
