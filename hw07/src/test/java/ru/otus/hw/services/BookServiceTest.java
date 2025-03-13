package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void findByIdTest(Long id) {
        var book = bookService.findById(id);

        assertThat(book).isPresent();
    }

    @Test
    void findBookRelationsTest() {
        var book = bookService.findById(1L);

        assertThat(book).isPresent();
        assertThat(book.get().getTitle()).isEqualTo("BookTitle_1");
        assertThat(book.get().getAuthor().getId()).isEqualTo(1L);
        assertThat(book.get().getGenre().getId()).isEqualTo(1L);
    }

    @Test
    void findAllBooksTest() {
        var books = bookService.findAll();

        assertThat(books).hasSize(3);
        assertThat(books.get(1).getTitle()).isEqualTo("BookTitle_2");
        assertThat(books.get(1).getAuthor().getId()).isEqualTo(2L);
        assertThat(books.get(1).getGenre().getId()).isEqualTo(2L);
    }

    @Test
    void insertBookTest() {
        var title = "BookTitle_4";
        var actualBook = bookService.insert(title, 1L, 1L);

        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getId()).isEqualTo(4L);
        assertThat(actualBook.getTitle()).isEqualTo(title);
        assertThat(actualBook.getAuthor().getId()).isEqualTo(1L);
        assertThat(actualBook.getGenre().getId()).isEqualTo(1L);
    }

    @Test
    void updateBookTest() {
        var title = "BookTitle_300";
        var actualBook = bookService.update(3L, title, 1L, 1L);

        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getId()).isEqualTo(3L);
        assertThat(actualBook.getTitle()).isEqualTo(title);
        assertThat(actualBook.getAuthor().getId()).isEqualTo(1L);
        assertThat(actualBook.getGenre().getId()).isEqualTo(1L);
    }

    @Test
    void deleteBookTest() {
        var actualBook = bookService.findById(3L);
        assertThat(actualBook).isPresent();

        bookService.deleteById(3L);

        actualBook = bookService.findById(3L);
        assertThat(actualBook).isEmpty();
    }

}
