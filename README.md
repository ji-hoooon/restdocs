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