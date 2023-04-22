package com.rest.docs;

import com.rest.docs.member.Member;
import com.rest.docs.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
//인수를 추가할 때마다 필수 생성자를 자동으로 추가줌
public class DataSetUp implements ApplicationRunner {
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final List<Member> members=new ArrayList<>();
        members.add(new Member("yun@bbb.com","yun"));
        members.add(new Member("jin@bbb.com","jin"));
        members.add(new Member("han@bbb.com","han"));
        members.add(new Member("jo@bbb.com","jo"));

        memberRepository.saveAll(members);

    }
}
