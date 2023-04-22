package com.rest.docs.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//웹에 대한 테스트를 모킹하기 위한 어노테이션
class MemberApiTest {
    /**
     * 1. Member 단일 조회
     * 2. Member 생성
     * 3. Member 수정
     * 4. Member 페이징 조회
     */

    @Autowired
    private MockMvc mvc;

    @Test
    public void member_page_test() throws Exception {
        //given
        mvc.perform(MockMvcRequestBuilders.get("/api/members")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //요청과 응답값 확인
                .andExpect(status().isOk()
                        //checkpoint: HttpStatus만 확인하는 불안정한 테스트 코드 -> response 값에 대해서 보완 필요
                );
    }

    @Test
    public void member_get_test() throws Exception {
        //조회 API -> 대상의 데이터가 반드시 필요하다. -> 정석대로면 given, when, then이 필요함
        //given
        mvc.perform(MockMvcRequestBuilders.get("/api/members{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //요청과 응답값 확인
                .andExpect(status().isOk()
                        //checkpoint: HttpStatus만 확인하는 불안정한 테스트 코드 -> response 값에 대해서 보완 필요
                );
    }

    @Test
    public void member_create_test() throws Exception {
        //given
        mvc.perform(post("/api/members/")
                        .contentType(MediaType.APPLICATION_JSON)
                        //요청에 바디가 전달되므로
                        .content(
                                "{\n" +
                                        "  \"email\": \"bbb@bbb.com\",\n" +
                                        "  \"name\": \"bbb\"\n" +
                                        "}"
                        )
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void member_modify_test() throws Exception {
        //given
        mvc.perform(put("/api/members/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        //요청에 바디가 전달되므로
                        .content(
                                "{\n" +
                                        "  \"name\": \"new_yun\"\n" +
                                        "}"
                        )
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}