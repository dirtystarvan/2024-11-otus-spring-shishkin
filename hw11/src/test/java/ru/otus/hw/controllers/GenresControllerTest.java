package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;


@WebFluxTest(GenresController.class)
class GenresControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreService service;

    @Test
    void getAllGenresTest() {
        List<Genre> genres = List.of(new Genre("1L", "Genre_1"), new Genre("2L", "Genre_2"));

        when(service.findAll()).thenReturn(Flux.fromIterable(genres));

        var result = webTestClient.get().uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(Genre.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNext(genres.get(0))
                .expectNext(genres.get(1))
                .verifyComplete();
    }

}