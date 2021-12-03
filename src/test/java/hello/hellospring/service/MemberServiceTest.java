package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("spring");

        // when
        Long savedId = memberService.join(member);

        // then
        Member findMember = memberService.findOne(savedId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("jwkim");

        Member member2 = new Member();
        member2.setName("jwkim");

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        // then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    void 모든_회원_찾기() {
        // given
        Member member1 = new Member();
        member1.setName("spring");
        memberService.join(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        memberService.join(member2);

        int memberCount = 2;

        // when
        List<Member> result = memberService.findMembers();

        // then
        assertThat(result).hasSize(memberCount);
    }

    @Test
    void 모든_회원_찾기_회원_없을때() {
        // given
        int memberCount = 0;

        // when
        List<Member> result = memberService.findMembers();

        // then
        assertThat(result).hasSize(memberCount);
    }


    @Test
    void ID로_회원_찾기() {
        // given
        Member member1 = new Member();
        member1.setName("spring");
        memberService.join(member1);

        // when
        Member result = memberService.findOne(member1.getId()).orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(member1.getId());
    }

    @Test
    void ID로_회원_찾기_회원_없을때() {
        // given
        Long id = 0L;

        // when
        Member result = memberService.findOne(id).orElse(null);

        // then
        assertThat(result).isNull();
    }
}