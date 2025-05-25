package ru.otus.hw.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.processors.AuthorProcessor;
import ru.otus.hw.processors.BookProcessor;
import ru.otus.hw.processors.GenreProcessor;
import ru.otus.hw.repositories.jpa.JpaAuthorRepository;
import ru.otus.hw.repositories.jpa.JpaBookRepository;
import ru.otus.hw.repositories.jpa.JpaGenreRepository;

import java.util.Collections;
import java.util.Map;

@Configuration
public class JobConfig {

    public static final String MIGRATION_JOB_NAME = "mongoMigration";

    private final Map<String, Sort.Direction> sort =
            Collections.singletonMap("id", Sort.Direction.ASC);

    @Bean
    public Job jpaToMongoMigration(JobRepository jobRepository,
                                   Flow splitFlow) {
        return new JobBuilder(MIGRATION_JOB_NAME, jobRepository)
                .start(splitFlow)
                .build()
                .build();
    }

    @Bean
    public Flow splitFlow(Flow authorsGenresFlow, Flow booksFlow,
                          @Qualifier("batchExecutor") TaskExecutor taskExecutor) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor)
                .add(authorsGenresFlow, booksFlow)
                .build();
    }

    @Bean
    public Flow authorsGenresFlow(Step migrateAuthors, Step migrateGenres) {
        return new FlowBuilder<SimpleFlow>("authorsGenresFlow")
                .start(migrateAuthors)
                .next(migrateGenres)
                .build();
    }

    @Bean
    public Flow booksFlow(Step migrateBooks) {
        return new FlowBuilder<SimpleFlow>("booksFlow")
                .start(migrateBooks)
                .build();
    }

    @Bean
    public Step migrateAuthors(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               RepositoryItemReader<JpaAuthor> jpaAuthorReader,
                               MongoItemWriter<MongoAuthor> mongoAuthorWriter,
                               AuthorProcessor authorProcessor) {
        return new StepBuilder("authorsMigration", jobRepository)
                .<JpaAuthor, MongoAuthor>chunk(10, transactionManager)
                .reader(jpaAuthorReader)
                .processor(authorProcessor)
                .writer(mongoAuthorWriter)
                .build();
    }

    @Bean
    public Step migrateGenres(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager,
                              RepositoryItemReader<JpaGenre> jpaGenreReader,
                              MongoItemWriter<MongoGenre> mongoGenreWriter,
                              GenreProcessor genreProcessor) {
        return new StepBuilder("genresMigration", jobRepository)
                .<JpaGenre, MongoGenre>chunk(10, transactionManager)
                .reader(jpaGenreReader)
                .processor(genreProcessor)
                .writer(mongoGenreWriter)
                .build();
    }

    @Bean
    public Step migrateBooks(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager,
                             RepositoryItemReader<JpaBook> jpaBookReader,
                             MongoItemWriter<MongoBook> mongoBookWriter,
                             BookProcessor bookProcessor) {
        return new StepBuilder("booksMigration", jobRepository)
                .<JpaBook, MongoBook>chunk(10, transactionManager)
                .reader(jpaBookReader)
                .processor(bookProcessor)
                .writer(mongoBookWriter)
                .build();
    }

    @Bean
    public RepositoryItemReader<JpaAuthor> jpaAuthorReader(JpaAuthorRepository jpaAuthorRepository) {
        RepositoryItemReader<JpaAuthor> reader = new RepositoryItemReader<>();
        reader.setName("jpaAuthorReader");

        reader.setSort(sort);
        reader.setRepository(jpaAuthorRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(10);
        return reader;
    }

    @Bean
    public RepositoryItemReader<JpaGenre> jpaGenreReader(JpaGenreRepository jpaGenreRepository) {
        RepositoryItemReader<JpaGenre> reader = new RepositoryItemReader<>();
        reader.setName("jpaGenreReader");

        reader.setSort(sort);
        reader.setRepository(jpaGenreRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(10);

        return reader;
    }

    @Bean
    public RepositoryItemReader<JpaBook> jpaBookReader(JpaBookRepository jpaBookRepository) {
        RepositoryItemReader<JpaBook> reader = new RepositoryItemReader<>();
        reader.setName("jpaBookReader");

        reader.setSort(sort);
        reader.setRepository(jpaBookRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(10);

        return reader;
    }

    @Bean
    public MongoItemWriter<MongoAuthor> mongoAuthorWriter(MongoTemplate mongoTemplate) {
        MongoItemWriter<MongoAuthor> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("authors");

        return writer;
    }

    @Bean
    public MongoItemWriter<MongoGenre> mongoGenreWriter(MongoTemplate mongoTemplate) {
        MongoItemWriter<MongoGenre> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("genres");

        return writer;
    }

    @Bean
    public MongoItemWriter<MongoBook> mongoBookWriter(MongoTemplate mongoTemplate) {
        MongoItemWriter<MongoBook> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("books");

        return writer;
    }

    @Bean
    public TaskExecutor batchExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
