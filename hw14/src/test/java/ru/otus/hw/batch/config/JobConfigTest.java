package ru.otus.hw.batch.config;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.hw.batch.config.JobConfig.MIGRATION_JOB_NAME;

@SpringBootTest
@SpringBatchTest
class JobConfigTest {

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MongoAuthorRepository mongoAuthorRepository;

    @Autowired
    private MongoGenreRepository mongoGenreRepository;

    @Autowired
    private MongoBookRepository mongoBookRepository;

    @BeforeEach
    void setUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void migrationJobTest() throws Exception {
        var job = jobLauncherTestUtils.getJob();

        assertNotNull(job);
        assertEquals(MIGRATION_JOB_NAME, job.getName());

        var execution = jobLauncherTestUtils.launchJob(new JobParameters());

        assertEquals("COMPLETED", execution.getExitStatus().getExitCode());

        var authorComparisonFields = RecursiveComparisonConfiguration.builder().withComparedFields("fullName").build();
        var genreComparisonFields = RecursiveComparisonConfiguration.builder().withComparedFields("name").build();
        var bookComparisonFields = RecursiveComparisonConfiguration.builder().withComparedFields("title", "author", "genre").build();

        var mongoAuthors = mongoAuthorRepository.findAll();

        assertThat(mongoAuthors)
                .isNotEmpty()
                .usingRecursiveComparison(authorComparisonFields)
                .isEqualTo(getAuthorReferenceList());

        var mongoGenres = mongoGenreRepository.findAll();

        assertThat(mongoGenres)
                .isNotEmpty()
                .usingRecursiveComparison(genreComparisonFields)
                .isEqualTo(getGenreReferenceList());

        var mongoBooks = mongoBookRepository.findAll();

        assertThat(mongoBooks)
                .isNotEmpty()
                .usingRecursiveComparison(bookComparisonFields)
                .isEqualTo(getBookReferenceList());
    }

    private List<MongoAuthor> getAuthorReferenceList() {
        return List.of(new MongoAuthor("Author_1"), new MongoAuthor("Author_2"), new MongoAuthor("Author_3"));
    }

    private List<MongoGenre> getGenreReferenceList() {
        return List.of(new MongoGenre("Genre_1"), new MongoGenre("Genre_2"), new MongoGenre("Genre_3"));
    }

    private List<MongoBook> getBookReferenceList() {
        return List.of(new MongoBook("BookTitle_1", getAuthorReferenceList().get(0), getGenreReferenceList().get(0)), new MongoBook("BookTitle_2", getAuthorReferenceList().get(1), getGenreReferenceList().get(1)), new MongoBook("BookTitle_3", getAuthorReferenceList().get(2), getGenreReferenceList().get(2)));
    }

    private List<MongoBook> getBookReferenceList(List<MongoAuthor> authorReferenceList, List<MongoGenre> genreReferenceList) {
        return List.of(new MongoBook("BookTitle_1", authorReferenceList.get(0), genreReferenceList.get(0)), new MongoBook("BookTitle_2", authorReferenceList.get(1), genreReferenceList.get(1)), new MongoBook("BookTitle_3", authorReferenceList.get(2), genreReferenceList.get(2)));
    }

}