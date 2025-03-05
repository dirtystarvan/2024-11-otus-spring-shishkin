package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(BooksController.class)
class BooksControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;

    @Test
    void getAllBooksTest() {
        List<Book> books = List.of(
                new Book("1L", "Book_1",
                    new Author("1L", "Author_1"),
                    new Genre("1L", "Genre_1")
                ),
                new Book("2L", "Book_2",
                        new Author("2L", "Author_2"),
                        new Genre("2L", "Genre_2")
                )
        );

        when(bookService.findAll()).thenReturn(Flux.fromIterable(books));

        var result = webTestClient.get().uri("/api/books")
                .exchange()
                .returnResult(Book.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNext(books.get(0))
                .expectNext(books.get(1))
                .verifyComplete();
    }

    @Test
    void getBookByIdTest() {
        Book book = new Book(
                "1", "Book_1",
                new Author("1L", "Author_1"),
                new Genre("1L", "Genre_1")
        );

        when(bookService.findById("1")).thenReturn(Mono.just(book));

        var result = webTestClient.get().uri("/api/books/1")
                .exchange()
                .returnResult(Book.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    void getBookByIdNotFoundTest() {
        when(bookService.findById("1")).thenReturn(Mono.empty());

        var result = webTestClient.get().uri("/api/books/1")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Book.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void createBookTest() {
        var book = new Book("1L", "Book_1",
                new Author("1L", "Author_1"), new Genre("1L", "Genre_1"));

        when(bookService.insert("Book_1", "1L", "1L")).thenReturn(Mono.just(book));

        var result = webTestClient.post()
                .uri("/api/books")
                .contentType(APPLICATION_JSON)
                .body(Mono.just(book), Book.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Book.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    void updateBookTest() {
        var book = new Book("1", "Book_1",
                new Author("1L", "Author_1"), new Genre("1L", "Genre_1"));

        when(bookService.update("1", "Book_1", "1L", "1L")).thenReturn(Mono.just(book));

        var result = webTestClient.patch()
                .uri("/api/books/1")
                .contentType(APPLICATION_JSON)
                .body(Mono.just(book), Book.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Book.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    void updateBookNoPathVariableTest() {
        webTestClient.patch().uri("/api/books")
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void deleteBookTest() {
        when(bookService.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/books/1").exchange()
                .expectStatus().isOk();
    }

    @Test
    void deleteBookNoPathVariableTest() {
        webTestClient.delete().uri("/api/books")
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

}
