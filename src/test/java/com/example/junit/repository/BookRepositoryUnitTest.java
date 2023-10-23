package com.example.junit.repository;

// 단위 테스트(DB 관련된 Bean이 IoC에 등록되면 됨)
/**
 *  Replace.ANY : 가짜 DB로 테스트
 *  Replace.NONE : 실제 DB로 테스트
 */

import com.example.junit.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY)
@DataJpaTest // Repository들을 다 IoC 등록해둠
public class BookRepositoryUnitTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void 책을_저장한다() {
        //given
        Book book = new Book(null, "title", "author");

        //when
        Book bookEntity = bookRepository.save(book);

        //then
        assertEquals("title", bookEntity.getTitle());
    }
}
