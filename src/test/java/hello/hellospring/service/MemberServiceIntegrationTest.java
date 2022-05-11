package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        System.out.println("회원가입 전 : " + memberRepository.findAll().size());
        //Given
        Member member = new Member();
        member.setName("hello1");

        //When
        Long saveId = memberService.join(member);

        //Then
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());

        System.out.println("회원가입 후 : " + memberRepository.findAll().size());
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        System.out.println("중복회원 : " + memberRepository.findAll().size());
        //Given
        Member member1 = new Member();
        member1.setName("spring123");
        Member member2 = new Member();
        member2.setName("spring123");

        //When
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2)); //예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        System.out.println("중복회원 후 : " + memberRepository.findAll().size());
    }
}
