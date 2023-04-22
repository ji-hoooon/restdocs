package com.rest.docs.member;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApi {
    private final MemberRepository memberRepository;

    /**
     * 1. Member 단일 조회
     * 2. Member 생성
     * 3. Member 수정
     * 4. Member 페이징 조회
     */
    @GetMapping("/{id}")
    //{id}로 받으면 PathVariable 필요
    public MemberResponse getMember(@PathVariable Long id) {
        //id가 없으면 발생하는 예외처리
        return new MemberResponse(memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Notfound")));
        //필요한 데이터만 노출하도록 엔티티가 아닌 DTO 생성 -> Inline Variable 단축키 사용
    }


    @PostMapping
    public void createMember(@RequestBody @Valid MemberSignUpRequest dto) {
        memberRepository.save(dto.toEntity());
        //Service 계층에서 Transaction을 걸어서 만들어야함
    }

    //이메일은 수정 안됨 -> 이름만 수정 가능
    @PutMapping("/{id}")
    public void modify(@PathVariable Long id, @RequestBody @Valid MemberModificationRequest dto) {
        final Member member = memberRepository.findById(id).get();
        member.modify(dto.getName());
        //명시적으로 save 필요
        memberRepository.save(member);
    }

    @GetMapping
    public Page<MemberResponse> getMembers(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberResponse::new);
        //page객체를 map을 통해 변환해서 DTO로 변경
    }
}
