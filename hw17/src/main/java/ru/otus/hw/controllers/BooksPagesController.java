package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BooksPagesController {

    private static final String EDIT_BOOK_VIEW_NAME = "editBook";

    private static final String ALL_BOOKS_VIEW_NAME = "allBooks";

    private static final String CREATE_BOOK_VIEW_NAME = "createBook";

    private static final String DELETE_BOOK_VIEW_NAME = "deleteBook";

    @GetMapping("/")
    public String allBooksPage() {
        return ALL_BOOKS_VIEW_NAME;
    }

    @GetMapping("/editBook/{id}")
    public String editBookPage() {
        return EDIT_BOOK_VIEW_NAME;
    }

    @GetMapping("/deleteBook/{id}")
    public String deleteBookPage() {
        return DELETE_BOOK_VIEW_NAME;
    }

    @GetMapping("/createBook")
    public String createBookPage() {
        return CREATE_BOOK_VIEW_NAME;
    }
}
