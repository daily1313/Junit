package com.example.junit.controller;

import com.example.junit.domain.Book;
import com.example.junit.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<?> save(@RequestBody final Book book) {
        return new ResponseEntity<Book>(bookService.저장하기(book), HttpStatus.CREATED);
    }

    @GetMapping("/books")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(bookService.모두가져오기(), HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<?> findById(@PathVariable final Long id) {
        return new ResponseEntity<Book>(bookService.한건조회하기(id), HttpStatus.OK);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<?> update(@PathVariable final Long id, @RequestBody final Book book) {
        return new ResponseEntity<>(bookService.수정하기(id, book), HttpStatus.OK);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.삭제하기(id), HttpStatus.OK);
    }
}
