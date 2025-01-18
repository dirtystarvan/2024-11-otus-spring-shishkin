package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.service.ClassPathResourceUtility;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {
    private static final String TEST_FILE_NAME = "questions_test.csv";

    private static CsvQuestionDao questionDao;

    @BeforeAll
    static void init(@Mock TestFileNameProvider fileNameProvider) {
        when(fileNameProvider.getTestFileName()).thenReturn(TEST_FILE_NAME);

        questionDao = new CsvQuestionDao(fileNameProvider, new ClassPathResourceUtility());
    }

    @Test
    void readQuestionsSuccess() {
        var questions = questionDao.findAll();

        assertNotNull(questions);
        assertFalse(questions.isEmpty());

        questions.forEach(question ->
                assertFalse(question.answers().isEmpty())
        );

        assertEquals(2, questions.size());

        var question = questions.get(0);
        var answers = question.answers();
        assertTrue(answers.get(0).isCorrect());
        assertEquals(3, answers.size());
        assertEquals("Is there life on Mars?", question.text());
        assertEquals("Science doesn't know this yet", answers.get(0).text());
        assertEquals("Certainly. The red UFO is from Mars. And green is from Venus", answers.get(1).text());
        assertEquals("Absolutely not", answers.get(2).text());

        question = questions.get(1);
        answers = question.answers();
        assertTrue(answers.get(0).isCorrect());
        assertEquals(3, answers.size());
        assertEquals("How should resources be loaded form jar in Java?", question.text());
        assertEquals("ClassLoader#getResourceAsStream or ClassPathResource#getInputStream", answers.get(0).text());
        assertEquals("ClassLoader#getResource#getFile + FileReader", answers.get(1).text());
        assertEquals("Wingardium Leviosa", answers.get(2).text());
    }
}
