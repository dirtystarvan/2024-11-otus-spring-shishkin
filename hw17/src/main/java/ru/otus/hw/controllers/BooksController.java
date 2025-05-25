package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
public class BooksController {

    private static final Function<Long, Supplier<EntityNotFoundException>> BOOK_NOT_FOUND_SUPPLIER =
            (Long id) -> () -> new EntityNotFoundException("Book with id %d not found".formatted(id));

    private final BookService bookService;

    @GetMapping("/api/books")
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/books/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.findById(id).orElseThrow(BOOK_NOT_FOUND_SUPPLIER.apply(id));
    }

    @PostMapping("/api/books")
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        bookService.insert(book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/books/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book) {
        bookService.update(id, book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
