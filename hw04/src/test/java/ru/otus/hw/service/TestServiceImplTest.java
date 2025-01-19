package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class TestServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private TestService testService;

    @Test
    void testResultForStudent() {
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(1);
        when(questionDao.findAll()).thenReturn(getQuestionList());

        var student = new Student("test", "test");

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
