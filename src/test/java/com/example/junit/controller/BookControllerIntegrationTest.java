package com.example.junit.controller;

// 통합 테스트 (모든 Bean들을 똑같이 IoC 올리고 테스트 하는 것)

/**
 *  webEnvironment =  WebEnvironment.MOCK = 실제 톰켓을 올리는게 아니라, 다른 톰켓으로 테스트
 *  webEnvironment =  WebEnvironment.RANDOM_PORT = 실제 톰켓으로 테스트
 * @AutoConfigureMockMvc MockMvc를 Ioc에 등록해줌
 * @Transactional 각각의 테스트함수가 종료될 때마다 transaction rollback
 */

import com.example.junit.domain.Book;
import com.example.junit.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;



import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment =  WebEnvironment.MOCK)
@ActiveProfiles("test")
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void init() {
        entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT=1").executeUpdate();
    }

    @Test
    void 책을_저장한다() throws Exception {
        //given (테스트를 하기 위한 준비)
        Book book = new Book(null, "title1", "daily1313");
        String content = new ObjectMapper().writeValueAsString(book);

        //when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then (검증)
        resultAction
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 책을_전체조회한다() throws Exception {
        //given
        bookRepository.saveAll(Arrays.asList(new Book(1L, "title1", "daily1313"),
                new Book(2L, "title2", "rlaehddnd0422")));

        //when
        ResultActions resultActions = mockMvc.perform(get("/books")
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.[0].title").value("title1"))
                .andExpect(jsonPath("$.[1].title").value("title2"))
                .andExpect(jsonPath("$.[0].author").value("daily1313"))
                .andExpect(jsonPath("$.[1].author").value("rlaehddnd0422"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 책을_단건조회한다() throws Exception {
        //given
        Long id = 2L;

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "title1", "daily1313"));
        books.add(new Book(2L, "title2", "rlaehddnd0422"));
        books.add(new Book(3L, "title3", "sosow0212"));
        bookRepository.saveAll(books);

        //when
        ResultActions resultAction = mockMvc.perform(get("/books/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title2"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 책을_수정한다() throws Exception {
        //given
        Long id = 3L;
        List<Book> books = new ArrayList<>();
        books.add(new Book(null, "title1", "daily1313"));
        books.add(new Book(null, "title2", "rlaehddnd0422"));
        books.add(new Book(null, "title3", "sosow0212"));
        bookRepository.saveAll(books);

        Book book = new Book(null, "C++", "javajoha");
        String content = new ObjectMapper().writeValueAsString(book);

        //when
        ResultActions resultAction = mockMvc.perform(put("/books/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("C++"))
                .andExpect(jsonPath("$.author").value("javajoha"))
                .andDo(MockMvcResultHandlers.print());
    }
}
