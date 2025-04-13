package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BooksController.class)
class BooksControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @Test
    void getAllBooksTest() throws Exception {
        List<Book> books = List.of(new Book(1L, "Book_1", new Author(1L, "Author_1"), new Genre(1L, "Genre_1")), new Book(2L, "Book_2", new Author(2L, "Author_2"), new Genre(2L, "Genre_2")));

        when(bookService.findAll()).thenReturn(books);

        mvc.perform(get("/api/books"))
                .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @Test
    void getBookByIdTest() throws Exception {
        Book book = new Book(1L, "Book_1", new Author(1L, "Author_1"), new Genre(1L, "Genre_1"));

        when(bookService.findById(1L)).thenReturn(Optional.of(book));

        mvc.perform(get("/api/books/1"))
                .andExpect(content().json(mapper.writeValueAsString(book)));
    }

    @Test
    void getBookByIdNotFoundTest() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/books/1"))
                .andExpect(view().name("errorPage"));
    }

    @Test
    void createBookTest() throws Exception {
        var json = mapper.writeValueAsString(new Book(1L, "Book_1", new Author(1L, "Author_1"), new Genre(1L, "Genre_1")));
        mvc.perform(post("/api/books").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void updateBookTest() throws Exception {
        var json = mapper.writeValueAsString(new Book(1L, "Book_1", new Author(1L, "Author_1"), new Genre(1L, "Genre_1")));

        mvc.perform(patch("/api/books/1").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void updateBookNoPathVariableTest() throws Exception {
        mvc.perform(patch("/api/books"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void deleteBookTest() throws Exception {
        mvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBookNoPathVariableTest() throws Exception {
        mvc.perform(delete("/api/books"))
                .andExpect(status().isMethodNotAllowed());
    }

}