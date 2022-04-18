package com.piotrsarencki.springrestapitests.service;

import com.piotrsarencki.springrestapitests.model.Book;
import com.piotrsarencki.springrestapitests.repo.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;


    @Test
    public void whenSaveBookShouldReturnBook() {
        Book book = new Book(1L, "Title", "Author", 200, 50.3);
        when(bookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(book);
        Book created = bookService.postBook(book);
        assertThat(created.getId()).isSameAs(book.getId());
        assertThat(created.getAuthor()).isSameAs(book.getAuthor());
        verify(bookRepository).save(book);
    }

    @Test
    public void shouldReturnAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book());
        given(bookRepository.findAll()).willReturn(books);
        List<Book> expected = bookService.getListOfBooks();
        assertEquals(expected, books);
        verify(bookRepository).findAll();
    }

    @Test
    public void whenGivenIdThenReturnBookIfFound() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        Book expected = bookService.getBookById(book.getId());

        assertThat(expected).isSameAs(book);
        verify(bookRepository).findById(book.getId());
    }

    @Test(expected = ResponseStatusException.class)
    public void shouldThrowExceptionIfBookNotFound() {
        Book book = new Book(1L, "Title", "Author", 200, 50.2);
        given(bookRepository.findById(anyLong())).willReturn(Optional.empty());
        bookService.getBookById(book.getId());

    }

    @Test
    public void whenGivenIdBookShouldDeleteIfFound() {
        Book book = new Book(1L, "Title", "Author", 200, 50.2);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        bookService.deleteBookById(book.getId());
        verify(bookRepository).deleteById(book.getId());
    }

    @Test(expected = ResponseStatusException.class)
    public void whenDeleteShouldReturnExceptionIfNotFound() {
        Book book = new Book(1L, "Title", "Author", 200, 50.2);
        given(bookRepository.findById(anyLong())).willReturn(Optional.empty());
        bookService.deleteBookById(book.getId());
    }


}