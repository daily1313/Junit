package com.example.junit.service;

//단위 테스트 (Service와 관련된 애들만 메모리에 띄우면 됨.)

import com.example.junit.domain.Book;
import com.example.junit.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 *  BookRepository -> 가짜 객체로 만들 수 있음
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

    @InjectMocks // BookService 객체가 만들어질 때 BookServiceUnitTest 파일에 @Mock으로 등록된 모든 애들을 주입받는다.
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void 책을_저장한다() {
        //given
        Book book = new Book(null, "title", "content");

        //when - stub -> 동작 지정
        when(bookRepository.save(book)).thenReturn(book);

        Book bookEntity = bookService.저장하기(book);

        //then
        assertEquals(bookEntity, book);
    }

}
