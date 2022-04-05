package com.team.veco.service;

import com.team.veco.configuration.security.jwt.JwtTokenProvider;
import com.team.veco.domain.Member;
import com.team.veco.dto.request.LoginDto;
import com.team.veco.dto.request.MemberRequestDto;
import com.team.veco.dto.request.PasswordDto;
import com.team.veco.dto.response.MemberResponseDto;
import com.team.veco.exception.exception.DuplicateMemberException;
import com.team.veco.exception.exception.MemberNotFindException;
import com.team.veco.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void init(){
        memberRepository.deleteAll();
    }

    @Test
    public void join(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "1234", "jojeayoung");

        //when
        Long join = memberService.join(memberRequestDto);
        MemberResponseDto one = memberService.findOne(join);

        //then
        Assertions.assertThat(one.getName()).isEqualTo("jojeayoung");
    }

    @Test
    public void notDuplicateJoinMember(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "1234", "jojeayoung");

        //when
        memberService.join(memberRequestDto);

        //then
        org.junit.jupiter.api.Assertions.assertThrows(DuplicateMemberException.class, () -> {
            memberService.join(memberRequestDto);
        });
    }

    @Test
    public void login(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "1234", "jojeayoung");
        memberService.join(memberRequestDto);
        LoginDto loginDto = new LoginDto("test@gmail.com", "1234");

        //when
        Map<String, String> login = memberService.login(loginDto);
        String accessToken = tokenProvider.getEmail(login.get("accessToken"));

        //then
        Assertions.assertThat(login.get("email")).isEqualTo(memberRequestDto.getEmail());
        Assertions.assertThat(accessToken).isEqualTo(memberRequestDto.getEmail());
    }

    @Test
    public void logout(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "1234", "jojeayoung");
        memberService.join(memberRequestDto);
        LoginDto loginDto = new LoginDto("test@gmail.com", "1234");
        login(loginDto, memberRequestDto);
        memberService.logout();

        //then
        org.junit.jupiter.api.Assertions.assertNull(memberRepository.findByEmail(memberRequestDto.getEmail()).orElseThrow().getRefreshToken());
    }

    @Test
    public void changePassword(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "1234", "jojeayoung");
        memberService.join(memberRequestDto);
        LoginDto loginDto = new LoginDto("test@gmail.com", "1234");
        login(loginDto, memberRequestDto);

        //when
        PasswordDto passwordChangeDto = new PasswordDto("12345");
        memberService.updatePassword(passwordChangeDto);

        //then
        Member member = memberRepository.findByEmail(MemberService.getUserEmail()).orElseThrow();
        Assertions.assertThat(passwordEncoder.matches(passwordChangeDto.getPassword(),member.getPassword())).isTrue();
    }

    @Test
    public void withdrawal(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@gmail.com", "1234", "jojeayoung");
        Long join = memberService.join(memberRequestDto);
        LoginDto loginDto = new LoginDto("test@gmail.com", "1234");
        login(loginDto, memberRequestDto);

        //when
        memberService.withdrawal();

        //then
        org.junit.jupiter.api.Assertions.assertThrows(MemberNotFindException.class, () -> memberService.findOne(join));
    }

    private void login(LoginDto loginDto, MemberRequestDto memberRequestDto) {
        memberService.login(loginDto);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                memberRequestDto.getEmail(),
                memberRequestDto.getPassword());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }
}