package com.piotrsarencki.springrestapitests.service;

import com.piotrsarencki.springrestapitests.model.Book;
import com.piotrsarencki.springrestapitests.repo.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getListOfBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    public Book postBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBookById(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id: " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

//    @PostConstruct
//    private List<Book> defaultData() {
//        List<Book> bookList = new ArrayList<>();
//        for (Long i = 1L; i < 101L; i++) {
//            Double price = (Math.floor(Math.random() * 10000) / 100);
//            int stock = (int) Math.floor(Math.random() * 100);
//            Book book = Book.builder()
//                    .id(i)
//                    .title("Title of Book " + i.toString())
//                    .author("Author Book " + i.toString())
//                    .price(price)
//                    .stock(stock)
//                    .build();
//            bookList.add(book);
//        }
//        bookRepository.saveAll(bookList);
//        return bookList;
//    }

}
