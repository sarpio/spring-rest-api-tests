package com.piotrsarencki.springrestapitests.rest;

import com.piotrsarencki.springrestapitests.model.Book;
import com.piotrsarencki.springrestapitests.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getListOfBooks();
    }

    @GetMapping("/book/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    public Book saveBook(@RequestBody Book book) {
        return bookService.postBook(book);
    }

    @DeleteMapping("/book/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return "Deleted with id: " + id;
    }
}
