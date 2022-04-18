package com.piotrsarencki.springrestapitests.rest;

import com.piotrsarencki.springrestapitests.model.Book;
import com.piotrsarencki.springrestapitests.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void createBookWithPostMethod() throws Exception {
        Book book = new Book(1L, "Title", "Author", 200, 50.3);

        given(bookService.postBook(book)).willReturn(book);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/book")
                .content("{\"id\":1,\"title\":\"Title\", \"author\":\"Author\", \"stock\":200,\"price\": 50.3}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void requestBookShouldReturnBook() throws Exception {
        Book book = new Book(1L, "Title", "Author", 200, 50.3);
        given(bookService.getBookById(book.getId())).willReturn(book);
        mockMvc.perform(get("/api/book/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void requestBookShouldThrowExceptionIfNotFound() throws Exception {
        Book book = new Book(1L, "Title", "Author", 200, 50.3);
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(bookService).getBookById(book.getId());
        mockMvc.perform(get("/api/book/" + book.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void requestAllBooksShouldReturnListOfBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Title 1", "Author 1", 200, 10.3));
        books.add(new Book(2L, "Title 2", "Author 2", 100, 20.3));
        books.add(new Book(3L, "Title 3", "Author 3", 400, 30.3));

        given(bookService.getListOfBooks()).willReturn(books);

        this.mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(books.size())));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        Book book = new Book(1L, "Title", "Author", 200, 50.3);
        given(bookService.getBookById(book.getId())).willReturn(book);
        doNothing().when(bookService).deleteBookById(book.getId());

        this.mockMvc.perform(delete("/api/book/{id}", book.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteBookById(book.getId());
    }
}