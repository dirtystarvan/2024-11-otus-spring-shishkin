package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(GenresController.class)
class GenresControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenreService service;

    @Test
    void getAllGenresTest() throws Exception {
        List<Genre> genres = List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"));

        when(service.findAll()).thenReturn(genres);

        mvc.perform(get("/api/genres"))
                .andExpect(content().json(objectMapper.writeValueAsString(genres)));
    }

}