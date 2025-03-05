package ru.otus.hw.repositories;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@EnableMongock
@DataMongoTest
class MongoGenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Test
    void findAllTest() {
        var actualGenres = genreRepository.findAll().collectList().block();
        var expectedGenres = mongoTemplate.findAll(Genre.class).collectList().block();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @Test
    void findByIdTest() {
        var expectedGenres = mongoTemplate.findAll(Genre.class).collectList().block();

        for (var genre : expectedGenres) {
            var actualGenre = genreRepository.findById(genre.getId()).block();
            assertThat(actualGenre).isEqualTo(genre);
        }
    }

}
