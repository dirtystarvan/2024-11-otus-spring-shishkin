package ru.otus.hw.repositories;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@EnableMongock
@DataMongoTest
class MongoAuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Test
    void testFindAll() {
        var actualAuthors = authorRepository.findAll().collectList().block();
        var expectedAuthors = mongoTemplate.findAll(Author.class).collectList().block();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @Test
    void testFindById() {
        var expectedAuthors = mongoTemplate.findAll(Author.class).collectList().block();

        for (var author : expectedAuthors) {
            var actualAuthor = authorRepository.findById(author.getId()).block();
            assertThat(actualAuthor).isEqualTo(author);
        }
    }

}
