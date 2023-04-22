package com.rest.docs.member;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class MemberSignUpRequest {

    //유효성 검사
    @Email
    private String email;

    @NotEmpty
    private String name;

    public Member toEntity(){
        return new Member(email, name);

    }
}
