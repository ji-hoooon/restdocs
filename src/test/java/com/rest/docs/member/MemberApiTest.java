package com.rest.docs.member;

import com.rest.docs.TestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
//import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
////웹에 대한 테스트를 모킹하기 위한 어노테이션
//@ExtendWith(RestDocumentationExtension.class)
////RestDoc 짜기 위해 필요한 클래스를 빈으로 등록하기 위한 어노테이션
class MemberApiTest extends TestSupport {
//리팩토링을 위해 작성한 클래스 상속
    /**
     * 1. Member 단일 조회
     * 2. Member 생성
     * 3. Member 수정
     * 4. Member 페이징 조회
     */

//    @Autowired
//    private MockMvc mvc;
//
//    @BeforeEach
//    void setUp(
//            final WebApplicationContext context,
//            final RestDocumentationContextProvider provider
//    ) {
//        this.mvc = MockMvcBuilders.webAppContextSetup(context)
//                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
//                .build();
//    }

    @Test
    public void member_page_test() throws Exception {
        //given
        mvc.perform(get("/api/members")
                        .param("size", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                )
//                .andDo(print()) //요청과 응답값 확인
//                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}"))
                .andExpect(status().isOk())//checkpoint: HttpStatus만 확인하는 불안정한 테스트 코드 -> response 값에 대해서 보완 필요
                .andDo(
                        restDocs.document(
                                //static import 진행
                                requestParameters(
                                        //쿼리 파라미터에 대한 문서 작성
                                        parameterWithName("size").optional().description("size"),
                                        parameterWithName("page").optional().description("page")
                        )));
    }

    @Test
    public void member_get_test() throws Exception {
        //조회 API -> 대상의 데이터가 반드시 필요하다. -> 정석대로면 given, when, then이 필요함
        //given
        mvc.perform(
                        get("/api/members/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
//                .andDo(print()) //요청과 응답값 확인
//                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}"))
                .andExpect(status().isOk())//checkpoint: HttpStatus만 확인하는 불안정한 테스트 코드 -> response 값에 대해서 보완 필요
                .andDo(
                        restDocs.document(
                                //static import 진행
                                pathParameters(
                                        //쿼리 파라미터에 대한 문서 작성
                                        parameterWithName("id").optional().description("Member ID")

                                ),
                                //응답 필드에 대한 문서 작성
                                responseFields(
                                        fieldWithPath("id").description("ID"),
                                        fieldWithPath("name").description("name"),
                                        fieldWithPath("email").description("email")
                                )
                        ));
    }

    @Test
    public void member_create_test() throws Exception {
        //given
        mvc.perform(post("/api/members/")
                        .contentType(MediaType.APPLICATION_JSON)
                        //요청에 바디가 전달되므로
                        .content(readJson("/json/member-api/member-create.json")
//                                readJson("/json/member-api/member-create.json")
//                                "{\n" +
//                                        "  \"email\": \"bbb@bbb.com\",\n" +
//                                        "  \"name\": \"bbb\"\n" +
//                                        "}"
                        )
                )
//                .andDo(print())
//                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        //optional이 아니라 필수 값이므로
                                        fieldWithPath("name").description("name"),
                                        fieldWithPath("email").description("email")
                                )
                        )
                );
    }

    @Test
    public void member_modify_test() throws Exception {
        //given
        mvc.perform(put("/api/members/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        //요청에 바디가 전달되므로
                        .content(readJson("/json/member-api/member-modify.json")
//                                "{\n" +
//                                        "  \"name\": \"new_yun\"\n" +
//                                        "}"
                        )
                )
//                .andDo(print())
//                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}"))
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                //static import 진행
                                pathParameters(
                                        //쿼리 파라미터에 대한 문서 작성
                                        parameterWithName("id").optional().description("Member ID")

                                ),
                                requestFields(
                                        //optional이 아니라 필수 값이므로
                                        fieldWithPath("name").description("name")
                                )
                        )
                );
    }
}