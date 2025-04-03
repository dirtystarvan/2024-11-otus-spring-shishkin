package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BooksPagesController.class)
class BooksPagesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void allBooksPageTest() throws Exception {
        mvc.perform(get("/"))
                .andExpect(view().name("allBooks"));
    }

    @Test
    void editBookPageTest() throws Exception {
        mvc.perform(get("/editBook/1"))
                .andExpect(view().name("editBook"));
    }

    @Test
    void deleteBookPageTest() throws Exception {
        mvc.perform(get("/deleteBook/1"))
                .andExpect(view().name("deleteBook"));
    }

    @Test
    void createBookPageTest() throws Exception {
        mvc.perform(get("/createBook"))
                .andExpect(view().name("createBook"));
    }


}