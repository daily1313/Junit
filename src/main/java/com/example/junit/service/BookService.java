package com.example.junit.service;

import com.example.junit.domain.Book;
import com.example.junit.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public Book 저장하기(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true) // JPA dirty Checking X
    public Book 한건조회하기(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book id를 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Book> 모두가져오기() {
        return bookRepository.findAll();
    }

    public Book 수정하기(Long id, Book book) {
        Book bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book id를 확인해주세요."));
        bookEntity.수정하기(book.getTitle(), book.getAuthor());
        return bookEntity;
    }

    public String 삭제하기(Long id) {
        bookRepository.deleteById(id);
        return "ok";
    }
}
