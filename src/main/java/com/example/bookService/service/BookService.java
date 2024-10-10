package com.example.bookService.service;

import com.example.bookService.models.Book;
import com.example.bookService.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

  private final BookRepository bookRepository;

  @Autowired
  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public Optional<Book> getBookById(Long id) {
    return bookRepository.findById(id);
  }

  public Book createBook(Book book) {
    return bookRepository.save(book);
  }

  public List<Book> listBooks() {
    return new ArrayList<>(bookRepository.findAll());
  }
}
