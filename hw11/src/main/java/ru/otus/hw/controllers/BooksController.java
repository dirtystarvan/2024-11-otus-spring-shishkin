package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public Flux<Book> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/books/{id}")
    public Mono<Book> getBookById(@PathVariable String id) {
        return bookService.findById(id);
    }

    @PostMapping("/api/books")
    public Mono<ResponseEntity<Book>> createBook(@RequestBody Book book) {
        return bookService.insert(book.getTitle(), book.getAuthor().getId(), book.getGenre().getId())
                .map(insertedBook -> ResponseEntity.ok().body(insertedBook));
    }

    @PatchMapping("/api/books/{id}")
    public Mono<ResponseEntity<Book>> updateBook(@PathVariable String id, @RequestBody Book book) {
        return bookService.update(id, book.getTitle(), book.getAuthor().getId(), book.getGenre().getId())
                .map(updatedBook -> ResponseEntity.ok().body(updatedBook));
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<ResponseEntity<?>> deleteBook(@PathVariable String id) {
        return bookService.deleteById(id).map(deletedBook -> ResponseEntity.ok().build());
    }

}
