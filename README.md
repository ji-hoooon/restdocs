# RESTDocs 학습을 위한 리포지토리

## 의존성 정보
```kts
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}
```

### RESTDocs
1. 테스트 코드 기반의 API 문서 작성 도구로, 테스트 코드가 일치하지 않으면 테스트 빌드가 실패
2. 따라서 테스트 기반으로 검증된 문서를 보장할 수 있다.
3. Swagger vs RestDocs
   - 명세보다는 호출이 쉽다.
   - API 명세는 REST Docs가 더 세부적이다.
   - Swagger의 경우 비즈니스 로직외에 다른 코드들이 많아서 가독성이 떨어진다.
   - 문서 갱신하지 않을 경우 API의 스펙과 문서가 일치하지 않을 수도 있다.
4. 테스트 코드 기반으로 검증되므로, 필드명 변경시 빌드가 실패해 신뢰가능한 문서가 된다.


### 메모
1. 직접 생성자로 LocalDateTime.now(), @OnPersist, auditing 기능을 사용하는 것의 차이
2. CommandLineRunner와 ApplicationRunner 차이


## 과정
1. member 엔티티 추가 후, 데이터 셋업 완료
2. http.api로 간단한 테스트 완료
3. 모킹 테스트로 Test Code 작성해 테스트 완료
   - import org.springframework.test.web.servlet.request.MockMvcRequestBuilders; -> O
   - import org.springframework.test.web.servlet.result.MockMvcResultMatchers; -> X
   - 자동으로 snippets 생성하기 위해 필요한 코드들
   ```java
   @ExtendWith(RestDocumentationExtension.class)
   //RestDoc 짜기 위해 필요한 클래스를 빈으로 등록하기 위한 어노테이션
   
   @BeforeEach
    void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .build();
    }
   
   .andDo(print()) //요청과 응답값 확인
   .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}"))
   .andExpect(status().isOk()
   ```
4. 테스트 클래스를 실행하면 자동으로 생성되는 Snippets 파일
   - 커스텀해서 추가적으로 생성도 가능
     - curl-request.adoc / httpie-request.adoc
     - http-request.adoc / http-response.adoc
     - request-body.adoc / response-body.adoc
5. index.adoc 작성
    - Member-API 작성
6. build.gradle에 CLU로 문서파일 만드는 테스크 빌드 정의
    ```yaml
    //테스크 빌드 정의한 CLU로 문서파일 생성
    tasks.test {
        outputs.dir(snippetsDir)
    }
    
    tasks.asciidoctor {
        dependsOn(tasks.test)
        val snippets = file("build/generated-snippets")
        configurations("asciidoctorExt")
        attributes["snippets"] = snippets
        inputs.dir(snippets)
        sources { include("**/index.adoc") }
        baseDirFollowsSourceFile()
    }
    ```
7. gradle에서 asciidoctor로 추정 문서 만든다.
   - gradle asciidoctor 수행
   - 추정 문서 :
     - Spring REST Docs에서 추정 문서란, 테스트 메서드를 실행하고 적용된 MockMvc의 결과를 바탕으로 자동으로 생성된 문서입니다. 추정 문서는 Spring REST Docs가 자동으로 생성하는 일부 문서 섹션을 말합니다. 일반적으로 추정 문서는 예제 요청과 응답 페이로드, 응답 헤더 및 응답 상태 코드를 보여주는 일반적인 REST API 응답에 대한 문서를 생성합니다.
     - 추정 문서는 대개 수동으로 작성해야하는 수고를 덜어주기 때문에 개발자들이 RESTful API의 문서화 작업을 효율적으로 할 수 있게 도와줍니다.
8. 로컬 서버에 생성된 문서를 웹 서버에 배포
   - 추정 문서를 웹 서버에 퍼블리싱하기 위한 코드
   ```yaml
   tasks.bootJar {
    dependsOn(tasks.asciidoctor)
    from("${tasks.asciidoctor.get().outputDir}") {
        into("static/docs")
     }
   }
   ```
   - gardle bootJar 수행
   - 수행되면 rest-docs-0.0.1-SNAPSHOT.jar 생성
   - cmd로 java -jar rest-docs-0.0.1-SNAPSHOT.jar 수행
   - localhost:8383/docs/index.html로 접근가능


## 리팩토링 대상
1. 중복 코드 제거
   - 어노테이션
   - 의존성 주입
   - MockMvc 세팅
   - 테스트 결과 출력
   - Snippets 생성
2. JSON 표현 개선
   - 단순 문자열로 JSON 처리
   - 복잡한 JSON의 경우에 유지보수가 어렵다.
3. JSON 포맷팅
   - 오른쪽으로 기울어지는 문제

## 리팩토링 수행
1. RestDocsConfiguration 작성
   - 테스트 메서드마다 반복되는 코드의 중복을 제거하고 
   - 제목 규칙을 설정하고, 문서를 식별하기 좋게 생성하도록 설정한다.
2. TestSupport 작성
   - RestDocs을 위한 테스트 설정 클래스을 임포트하고 테스트 코드를 쉽게 작성하기 위한 클래스로 관례적인 코드를 모아둔다.
3. 작성한 클래스를 이용해서 테스트 리팩토링
   - TestSupport를 상속해서 반복적이고 관례적인 코드를 제거한다.
4. JSON을 위한 resources 파일 작성
   - json.member-api 디렉토리 생성해 각각의 테스트 메서드에 필요한 JSON 작성
5. 최종 확인을 위해 gradle asciidoctor -x test 수행
6. dependendcies에 추가하지 않아서 생기는 에러
   ```yaml
   asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
   ```



## 문서 커스텀이 필요한 이유
- API 스펙 전달에 어려움이 존재하는 현재 문서
- 필요한 형식
  1. Request Fields (API 컨텍스트를 정확하게 전달하기 위해)
     - Field
     - Type
     - Required
     - Description
     - Length
  2. HTTP request
     - Path Parameters
  3. HTTP response
     - Response Fields
       - Field
       - Type
       - Required
       - Description
       - Length

## 문서 커스텀하기 (1)
1. RestDocs 기반으로 문서 작성을 위해 import 패키지 변경
   - import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get; 
   - import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post; 
   - import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
2. 상속한 TestSupport 클래스의 RestDocumentationResultHandler 변수를 이용해 문서 작성
   - restDocs.document()로 문서 작성 시작
   - pathParameters / parameterWithName
   - requestFields / fieldWithPath
   - responseFields / fieldWithPath
3. 필수값 여부 추가를 위해서는 스니펫 템플릿 변경 필요
```yaml
# /src/test/resources/org/springframework/restdocs/templates


# request-fields.snippet
|===
|Field|Type|Required|Description|Length
{{#fields}}
|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{^optional}}true{{/optional}}{{#optional}}false{{/optional}}{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}
|{{#tableCellContent}}{{#length}}{{.}}{{/length}}{{/tableCellContent}}
{{/fields}}
|===

# request-parameters.snippet
|===
|Parameter|Required|Description
{{#parameters}}
|{{#tableCellContent}}`+{{name}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{^optional}}true{{/optional}}{{#optional}}false{{/optional}}{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}
{{/parameters}}
|===


# response-fields.snippet

|===
|Path|Type|Required|Description
{{#fields}}
|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{^optional}}true{{/optional}}{{#optional}}false{{/optional}}{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}
{{/fields}}
|===
```
4. gradle clean asciidoctor
5. 기능별 API의 분리 필요

## 문서 커스텀하기 (2)
1. 인텔리제이 기능을 이용한 추출
   - option+enter : Extract Include Directive
2. API 서버에 대한 정보를 제공하는 overview
   - HOST 정보
   - HTTP 상태코드 정보
   - 커넥션 타임아웃 정보
   - 비즈니스 용어 사전 정보
3. 에러 응답 처리
   - 클래스 정의
     - ErrorCode
     - ErrorResponse
       - 400, 500과 같은 에러에 대한 포맷팅
       - 여러 가지 에러 사유를 전달하기 위해 배열로 전달
       - 실무에서는 에러에 해당하는 code를 통해 에러 파악을 용이하게 한다.
     - GlobalExceptionHandler
       - 다른 예외로는 에러가 나지 않도록 컨트롤한다.
       - 애플리케이션에서 예외 발생을 글로벌로 처리해주는 핸들러
       - 유효성 검사 실패, 그 외 예외를 다루는 메서드 작성
   - 테스트 코드에서만 사용할 API 클래스 작성
   - 필수값에 빈 문자열과 이메일 형식을 틀린 에러테스트
    ```json
    {
      "message": " Invalid Input Value",
      "status": 400,
      "errors": [
        {
          "field": "name",
          "value": "",
          "reason": "must not be empty"
        },
        {
          "field": "email",
          "value": "zobbb.com",
          "reason": "must be a well-formed email address"
        }
      ],
      "code": "C001",
      "timestamp": "2023-04-25T11:02:05.0944"
    }
    ```
   - 문서화를 위한 에러 템플릿
    ```java
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
    ```
   - 오버뷰에 에러 응답 추가
    ```json
    [[overview-error-response]]
    === HTTP Error Response
    
    operation::rest-document/sample-request[snippets='http-response,response-fields']   
    ```
   - 컬렉션의 경우 null이 아니라 빈 배열로 리턴해주는게 화면처리가 더 쉽다.