package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao questionDao;

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
