package ru.otus.hw.repositories;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@EnableMongock
@DataMongoTest
class MongoBookRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testFindById() {
        var expectedBooks = mongoTemplate.findAll(Book.class);

        for (var book : expectedBooks) {
            var actualBook = bookRepository.findById(book.getId());
            assertThat(actualBook).isPresent();
            assertThat(actualBook.get()).isEqualTo(book);
        }
    }

    @Test
    void testExistsById() {
        var expectedBooks = mongoTemplate.findAll(Book.class);

        for (var book : expectedBooks) {
            var isExists = bookRepository.existsById(book.getId());
            assertThat(isExists).isTrue();
        }
    }

    @Test
    void testFindAll() {
        var expectedBooks = mongoTemplate.findAll(Book.class);
        var actualBooks = bookRepository.findAll();

        assertThat(actualBooks).isNotEmpty();
        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
    }

    @Test
    void testSaveInsert() {
        var testBook = getTestBook();
        var persistedBook = bookRepository.insert(testBook);

        assertThat(persistedBook).isNotNull()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(testBook);

        assertThat(mongoTemplate.findById(persistedBook.getId(), Book.class)).isEqualTo(persistedBook);
    }

    @Test
    void testSaveUpdate() {
        var persistedBook = persistTestBook();

        var testUpdTitle = "updated";
        persistedBook.setTitle(testUpdTitle);
        bookRepository.save(persistedBook);

        assertThat(mongoTemplate.findById(persistedBook.getId(), Book.class)).isNotNull()
                .matches(book -> testUpdTitle.equals(book.getTitle()));
    }

    @Test
    void testDeleteById() {
        var persistedBook = persistTestBook();

        assertThat(mongoTemplate.findById(persistedBook.getId(), Book.class)).isNotNull();
        bookRepository.deleteById(persistedBook.getId());
        assertThat(mongoTemplate.findById(persistedBook.getId(), Book.class)).isNull();
    }

    private Book getTestBook() {
        var testGenre = new Genre("testGenre");
        var persistedGenre = mongoTemplate.save(testGenre);

        var testAuthor = new Author("testAuthor");
        var persistedAuthor = mongoTemplate.save(testAuthor);

        return new Book("testBook", persistedAuthor, persistedGenre);
    }

    private Book persistTestBook() {
        return mongoTemplate.insert(getTestBook());
    }

}
