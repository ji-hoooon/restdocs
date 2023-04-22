plugins {
    id("org.springframework.boot") version "2.5.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.asciidoctor.jvm.convert") version "3.1.0"
//    AsciiDoctor를 사용하여 문서를 생성할 수 있게 됩니다.
    id("java")
}

//group과 version은 Maven이나 Gradle에서 프로젝트를 식별하는 데 사용되는 정보를 설정합니다. java.sourceCompatibility는 Java 소스 코드의 버전을 지정합니다.
group = "com.rest.docs"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11



//configurations 블록은 Spring Restdocs에서 snippets를 생성할 때 필요한 의존성을 설정합니다.
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
lateinit var asciidoctorExt: Configuration
val snippetsDir = file("build/generated-snippets")

asciidoctorj {
    asciidoctorExt = configurations.create("asciidoctorExt")
}

//repositories 블록은 Maven Repository의 중앙 저장소에서 의존성을 가져옵니다.
repositories {
    mavenCentral()
}

//dependencies 블록에서는 Spring Boot, Spring Data JPA, Lombok, H2 데이터베이스, 그리고 Restdocs 테스트를 위한 라이브러리들을 추가합니다.
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // compileOnly configuration은 annotationProcessor를 상속하여 사용되며, Lombok과 같은 라이브러리는 컴파일 시점에만 사용되므로 compileOnly로 설정합니다.
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

//tasks 블록에서는 먼저 useJUnitPlatform() 메소드를 사용하여 JUnit 5로 테스트를 실행하도록 설정합니다.
tasks.withType<Test> {
    useJUnitPlatform()
}

// tasks.bootJar 블록에서는 Asciidoctor 플러그인을 실행하도록 지정하여 문서를 생성한다.
//: 로컬에 생성한 문서를 웹서버에 배포하기 위한 작업
tasks.bootJar {
    dependsOn(tasks.asciidoctor)
    //bootJar가 asciidoctor 실행
    from("${tasks.asciidoctor.get().outputDir}") {
        into("static/docs")
        //인덱스 파일을 생성해 해당 경로로 이동시킨다.
    }
}
//tasks.test 블록에서는 Restdocs 플러그인이 생성할 snippets 디렉터리를 정의합니다.
//테스크 빌드 정의한 CLU로 문서파일 생성
tasks.test {
    outputs.dir(snippetsDir)
}
//마지막으로, tasks.asciidoctor 블록에서는 Restdocs 플러그인에서 생성한 snippets를 사용하여 AsciiDoctor를 실행하여 API 문서를 생성합니다.
tasks.asciidoctor {
    dependsOn(tasks.test)
    //    tasks.asciidoctor 블록은 tasks.test 블록에 의존합니다.
    val snippets = file("build/generated-snippets")
    configurations("asciidoctorExt")
    attributes["snippets"] = snippets
    inputs.dir(snippets)
    //tasks.asciidoctor 블록에서는 snippets 디렉터리를 attributes 맵에 설정하여,
    // AsciiDoctor 플러그인이 snippets 디렉터리에서 snippets를 참조할 수 있도록 합니다.

    sources { include("**/index.adoc") }
    // 또한, sources에서는 index.adoc 파일을 포함하여 문서를 생성할 소스 파일을 정의합니다.
    baseDirFollowsSourceFile()
    // baseDirFollowsSourceFile() 메소드는 index.adoc 파일이 있는 디렉토리를 기준으로 문서를 생성하도록 설정합니다.
}



