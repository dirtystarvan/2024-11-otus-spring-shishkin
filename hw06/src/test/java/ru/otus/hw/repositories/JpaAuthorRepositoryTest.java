package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuthorRepository.class)
class JpaAuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindAll() {
        var actualAuthors = authorRepository.findAll();
        var expectedAuthors = entityManager.getEntityManager()
                .createQuery("select a from Author a").getResultList();

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void testFindById(long id) {
        var actualAuthor = authorRepository.findById(id);
        var expectedAuthor = entityManager.find(Author.class, id);

        assertThat(actualAuthor).isPresent();
        assertThat(actualAuthor.get()).isEqualTo(expectedAuthor);
    }

}