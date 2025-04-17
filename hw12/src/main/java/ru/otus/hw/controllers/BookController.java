package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private static final String EDIT_BOOK_VIEW_NAME = "editBook";

    private static final String ALL_BOOKS_VIEW_NAME = "allBooks";

    private static final String CREATE_BOOK_VIEW_NAME = "createBook";

    private static final String DELETE_BOOK_VIEW_NAME = "deleteBook";

    private static final String REDIRECT_ROOT = "redirect:/";

    private static final Function<Long, Supplier<EntityNotFoundException>> BOOK_NOT_FOUND_SUPPLIER =
            (Long id) -> () -> new EntityNotFoundException("Book with id %d not found".formatted(id));

    private final BookService bookService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping("/")
    public String allBooksPage(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);

        return ALL_BOOKS_VIEW_NAME;
    }

    @GetMapping("/editBook")
    public String editBookPage(@RequestParam("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(BOOK_NOT_FOUND_SUPPLIER.apply(id));
        var authors = authorService.findAll();
        var genres = genreService.findAll();

        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);

        return EDIT_BOOK_VIEW_NAME;
    }

    @GetMapping("/deleteBook")
    public String deleteBookPage(@RequestParam("id") long id, Model model) {
        var book = bookService.findById(id).orElseThrow(BOOK_NOT_FOUND_SUPPLIER.apply(id));

        model.addAttribute("book", book);

        return DELETE_BOOK_VIEW_NAME;
    }

    @GetMapping("/createBook")
    public String createBookPage(Model model) {
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        var book = new Book();

        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("book", book);

        return CREATE_BOOK_VIEW_NAME;
    }

    @PostMapping("/editBook")
    public String updateBook(@ModelAttribute("book") Book book) {
        bookService.update(book.getId(), book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId());

        return REDIRECT_ROOT;
    }

    @PostMapping("/createBook")
    public String createBook(@ModelAttribute("book") Book book) {
        bookService.insert(book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId());

        return REDIRECT_ROOT;
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@ModelAttribute("book") Book book) {
        bookService.deleteById(book.getId());

        return REDIRECT_ROOT;
    }

}
