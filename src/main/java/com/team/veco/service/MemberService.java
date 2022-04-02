package com.team.veco.service;

import com.team.veco.configuration.security.jwt.JwtTokenProvider;
import com.team.veco.domain.Member;
import com.team.veco.dto.request.LoginDto;
import com.team.veco.dto.request.MemberRequestDto;
import com.team.veco.enums.Role;
import com.team.veco.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public Long join(MemberRequestDto memberDto){
        String password = passwordEncoder.encode(memberDto.getPassword());
        Member member = memberDto.toEntity(password);
        return memberRepository.save(member).getId();
    }

    public Map<String, String> login(LoginDto loginDto){
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(()->new RuntimeException());
        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())){
            throw new RuntimeException();
        }
        String accessToken = tokenProvider.createToken(member.getEmail(), Role.MEMBER);
        String refreshToken = tokenProvider.createRefreshToken();
        String email = member.getEmail();

        Map<String, String> token=new HashMap<>();
        token.put("accessToken", accessToken);
        token.put("refreshToken", refreshToken);
        token.put("email", email);

        member.updateRefreshToken(refreshToken);

        return token;
    }

    public void logout(){
        String userEmail = getUserEmail();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException());
        member.updateRefreshToken(null);
    }

    public void withdrawal(){
        String userEmail = getUserEmail();
        memberRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException());
    }

    static public String getUserEmail() {
        String userEmail;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else {
            userEmail = principal.toString();
        }
        return userEmail;
    }
}