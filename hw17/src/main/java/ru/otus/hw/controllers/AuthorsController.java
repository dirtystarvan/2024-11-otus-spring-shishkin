package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public List<Author> getAllAuthors() {
        return authorService.findAll();
    }

}
