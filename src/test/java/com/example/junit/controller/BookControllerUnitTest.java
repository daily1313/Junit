package com.example.junit.controller;

// 단위 테스트 (Controller 관련 로직만 띄우기) Filter, ControllerAdvice


import com.example.junit.domain.Book;
import com.example.junit.repository.BookRepository;
import com.example.junit.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.hamcrest.Matchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@WebMvcTest(BookController.class)

public class BookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // IoC 환경에 Bean 등록
    private BookService bookService;

    // BDDMockito 패턴 given, when, then
    // should 접근법: "어떤 상황에서 어떠한 것이 발생해야 한다"라는 문장 형태
    @Test
    void 책을_저장한다() throws Exception {
        //given
        Book book = new Book(null, "title", "daily1313");
        String content = new ObjectMapper().writeValueAsString(book);
        when(bookService.저장하기(any())).thenReturn(new Book(1L, "title", "daily1313"));

        //when
        ResultActions resultAction = mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.author").value("daily1313"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 책을_전체조회한다() throws Exception {
        //given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "title1", "daily1313"));
        books.add(new Book(2L, "title2", "rlaehddnd0422"));
        books.add(new Book(3L, "title3", "sosow0212"));
        when(bookService.모두가져오기()).thenReturn(books);

        //when
        ResultActions resultActions = mockMvc.perform(get("/books")
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.[0].title").value("title1"))
                .andExpect(jsonPath("$.[1].title").value("title2"))
                .andExpect(jsonPath("$.[2].title").value("title3"))
                .andExpect(jsonPath("$.[0].author").value("daily1313"))
                .andExpect(jsonPath("$.[1].author").value("rlaehddnd0422"))
                .andExpect(jsonPath("$.[2].author").value("sosow0212"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 책을_단건조회한다() throws Exception {
        //given
        Long id = 1L;
        when(bookService.한건조회하기(any())).thenReturn(new Book(1L, "title", "daily1313"));

        //when
        ResultActions resultAction = mockMvc.perform(get("/books/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 책을_수정한다() throws Exception {
        //given
        Long id = 1L;
        Book book = new Book(null, "title", "daily1313");
        String content = new ObjectMapper().writeValueAsString(book);
        when(bookService.수정하기(id, book)).thenReturn(new Book(1L, "title", "daily1313"));

        //when
        ResultActions resultAction = mockMvc.perform(put("/books/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 책을_삭제한다() throws Exception {
        //given
        Long id = 1L;
        when(bookService.삭제하기(id)).thenReturn("ok");

        //when
        ResultActions resultAction = mockMvc.perform(delete("/books/{id}", id)
                .accept(MediaType.TEXT_PLAIN));

        //then
        resultAction
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        MvcResult requestResult = resultAction.andReturn();
        String result = requestResult.getResponse().getContentAsString();

        assertEquals("ok", result);
    }
}
