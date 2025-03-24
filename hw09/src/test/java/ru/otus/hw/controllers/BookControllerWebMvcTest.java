package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerWebMvcTest {

    private static final List<Book> BOOKS = List.of(new Book(1L, "Book_1", new Author(1L, "Author_1"), new Genre(1L, "Genre_1")),
            new Book(2L, "Book_2", new Author(2L, "Author_2"), new Genre(2L, "Genre_2")));

    private static final List<Author> AUTHORS = List.of(new Author(1L, "Author_1"), new Author(2L, "Author_2"));

    private static final List<Genre> GENRES = List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"));

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    void allBooksPageTest() throws Exception {
        when(bookService.findAll()).thenReturn(BOOKS);

        mvc.perform(get("/"))
                .andExpect(view().name("allBooks"))
                .andExpect(model().attribute("books", BOOKS));
    }

    @Test
    void editBookPageTest() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(BOOKS.get(0)));
        when(authorService.findAll()).thenReturn(AUTHORS);
        when(genreService.findAll()).thenReturn(GENRES);

        mvc.perform(get("/editBook").param("id", "1"))
                .andExpect(view().name("editBook"))
                .andExpect(model().attribute("book", BOOKS.get(0)))
                .andExpect(model().attribute("authors", AUTHORS))
                .andExpect(model().attribute("genres", GENRES));
    }

    @Test
    void deleteBookPageTest() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(BOOKS.get(0)));

        mvc.perform(get("/deleteBook").param("id", "1"))
                .andExpect(view().name("deleteBook"))
                .andExpect(model().attribute("book", BOOKS.get(0)));
    }

    @Test
    void createBookPageTest() throws Exception {
        when(authorService.findAll()).thenReturn(AUTHORS);
        when(genreService.findAll()).thenReturn(GENRES);

        mvc.perform(get("/createBook"))
                .andExpect(view().name("createBook"))
                .andExpect(model().attribute("authors", AUTHORS))
                .andExpect(model().attribute("genres", GENRES));
    }

    @Test
    void updateBookPostTest() throws Exception {
        var testBook = BOOKS.get(0);
        mvc.perform(post("/editBook").flashAttr("book", testBook))
                        .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).update(
                testBook.getId(), testBook.getTitle(), testBook.getAuthor().getId(), testBook.getGenre().getId()
        );
    }

    @Test
    void createBookPostTest() throws Exception {
        var testBook = BOOKS.get(0);
        mvc.perform(post("/createBook").flashAttr("book", testBook))
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).insert(
                testBook.getTitle(), testBook.getAuthor().getId(), testBook.getGenre().getId()
        );
    }

    @Test
    void deleteBookPostTest() throws Exception {
        var testBook = BOOKS.get(0);
        mvc.perform(post("/deleteBook").flashAttr("book", testBook))
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).deleteById(testBook.getId());
    }

    @Test
    void deleteBookWithExceptionTest() throws Exception {
        var testErrorMessage = "test message";
        when(bookService.findById(anyLong())).thenThrow(new EntityNotFoundException(testErrorMessage));

        mvc.perform(get("/deleteBook").param("id", "10"))
                .andExpect(view().name("errorPage"))
                .andExpect(model().attribute("message", testErrorMessage));
    }

}
