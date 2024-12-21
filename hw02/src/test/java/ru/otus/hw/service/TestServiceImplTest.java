package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    private final AnswerService answerService = new AnswerServiceImpl();


    @Test
    void testResultForStudent(@Mock Student student) {
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(1);
        when(questionDao.findAll()).thenReturn(getQuestionList());

        var testService = new TestServiceImpl(ioService, questionDao, answerService);

        var testResult = testService.executeTestFor(student);

        assertNotNull(testResult);
        assertEquals(student, testResult.getStudent());

        assertEquals(1, testResult.getRightAnswersCount());
        assertEquals(2, testResult.getAnsweredQuestions().size());
    }

    private List<Question> getQuestionList() {
        return List.of(
                new Question("test question 1", List.of(new Answer("answer1", true))),
                new Question("test question 2", List.of(new Answer("answer2", false)))
        );
    }

}
