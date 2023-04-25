package com.rest.docs;

import org.junit.jupiter.api.Test;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestDocumentApiTest extends TestSupport{

    @Test
    public void sampleRequest_test() throws Exception{
        //given
        mvc.perform(post("/test/sample")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"email\": \"zobbb.com\",\n" +
                        "  \"name\": \"\"\n" +
                        "}"
                )//이메일 형식 오류 이름 빈문자.
        ).andExpect(status().isBadRequest())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("message").description("에러 메시지"),
                                fieldWithPath("status").description("Http Status Code"),
                                fieldWithPath("code").description("Error Code"),
                                fieldWithPath("timestamp").description("에러 발생 시간"),
                                fieldWithPath("errors").description("Error 값 배열 값"),
                                fieldWithPath("errors[0].field").description("문제 있는 필드"),
                                fieldWithPath("errors[0].value").description("문제가 있는 값"),
                                fieldWithPath("errors[0].reason").description("문재가 있는 이유")
                        )
                ));


        //when

        //then

    }
}
