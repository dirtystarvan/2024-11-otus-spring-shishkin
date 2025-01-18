package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.ResourceUtility;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private static final String ERROR_MESSAGE = "Error while questions read";

    private final TestFileNameProvider fileNameProvider;

    private final ResourceUtility resourceUtility;

    @Override
    public List<Question> findAll() {

        var questions = Collections.<QuestionDto>emptyList();

        try (var questionsReader = resourceUtility.getResourceReader(fileNameProvider.getTestFileName())) {
            questions = new CsvToBeanBuilder<QuestionDto>(questionsReader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();
        } catch (Exception e) {
            throw new QuestionReadException(ERROR_MESSAGE, e);
        }

        return questions.stream().map(QuestionDto::toDomainObject).toList();
    }

}
