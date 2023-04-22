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
      - 