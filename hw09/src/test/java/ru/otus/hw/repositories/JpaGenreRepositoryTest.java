package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class JpaGenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findAllTest() {
        var actualGenres = genreRepository.findAll();
        var expectedGenres = entityManager.getEntityManager()
                .createQuery("select g from Genre g", Genre.class).getResultList();

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void findBiIdsTest(long id) {
        var actualGenre = genreRepository.findById(id);
        var expectedGenre = entityManager.find(Genre.class, id);

        assertTrue(actualGenre.isPresent());
        assertThat(actualGenre.get()).isEqualTo(expectedGenre);
    }

}