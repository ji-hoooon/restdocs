package com.rest.docs;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/test")
public class RestDocumentApi {
    //에러를 발생시켜서 에러 응답을 문서화하기 위한 API
    @PostMapping("/sample")
    public void sample(@RequestBody @Valid SampleRequest dto){

    }
    public static class SampleRequest{
        @NotEmpty
        private String name;
        @Email
        private String email;

        public SampleRequest(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
