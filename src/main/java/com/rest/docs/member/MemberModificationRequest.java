package com.rest.docs.member;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberModificationRequest {
    //이메일은 수정이 안됨

    @NotEmpty
    @Size(max = 10)
    private String name;
}
