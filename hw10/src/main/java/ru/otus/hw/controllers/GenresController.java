package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenresController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public List<Genre> getAllGenres() {
        return genreService.findAll();
    }
}
