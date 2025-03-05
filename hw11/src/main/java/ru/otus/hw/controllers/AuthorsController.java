package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

@RestController
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public Flux<Author> getAllAuthors() {
        return authorService.findAll();
    }

}
