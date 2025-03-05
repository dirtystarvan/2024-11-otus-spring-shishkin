package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(AuthorsController.class)
class AuthorsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorService authorService;

    @Test
    void getAllAuthorsTest() {
        List<Author> authors = List.of(new Author("1L", "Author_1"), new Author("2L", "Author_2"));

        when(authorService.findAll()).thenReturn(Flux.fromIterable(authors));

        var result = webTestClient.get().uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(Author.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNext(authors.get(0))
                .expectNext(authors.get(1))
                .verifyComplete();
    }

}
