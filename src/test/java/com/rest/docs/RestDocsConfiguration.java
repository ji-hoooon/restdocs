package com.rest.docs;

import antlr.preprocessor.Preprocessor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

//테스트 메서드마다 반복되는 코드의 중복을 제거하고
//제목 규칙을 설정하고, 문서를 식별하기 좋게 생성하도록 설정한다.
@TestConfiguration
public class RestDocsConfiguration {
    @Bean
    public RestDocumentationResultHandler write(){
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}",

                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        );
    }
}
