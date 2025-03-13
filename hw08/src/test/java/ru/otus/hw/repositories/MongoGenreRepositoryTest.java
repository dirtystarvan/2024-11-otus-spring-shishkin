package ru.otus.hw.repositories;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableMongock
@DataMongoTest
class MongoGenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void findAllTest() {
        var actualGenres = genreRepository.findAll();
        var expectedGenres = mongoTemplate.findAll(Genre.class);

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @Test
    void findByIdTest() {
        var expectedGenres = mongoTemplate.findAll(Genre.class);

        for (var genre : expectedGenres) {
            var actualGenre = genreRepository.findById(genre.getId());
            assertTrue(actualGenre.isPresent());
            assertThat(actualGenre.get()).isEqualTo(genre);
        }
    }

}
