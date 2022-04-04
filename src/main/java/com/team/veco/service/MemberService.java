package com.team.veco.service;

import com.team.veco.configuration.security.jwt.JwtTokenProvider;
import com.team.veco.domain.EmailCertificate;
import com.team.veco.domain.Member;
import com.team.veco.dto.request.EmailCertificateDto;
import com.team.veco.dto.request.LoginDto;
import com.team.veco.dto.request.MemberRequestDto;
import com.team.veco.dto.request.PasswordChangeDto;
import com.team.veco.dto.response.MemberResponseDto;
import com.team.veco.enums.Role;
import com.team.veco.exception.ErrorCode;
import com.team.veco.exception.exception.DuplicateMemberException;
import com.team.veco.exception.exception.MemberNotFindException;
import com.team.veco.exception.exception.PasswordNotCorrectException;
import com.team.veco.repository.EmailCertificateRepository;
import com.team.veco.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final EmailCertificateRepository emailCertificateRepository;

    public Long join(MemberRequestDto memberDto){
        if(!memberRepository.findByEmail(memberDto.getEmail()).isEmpty()){
            throw new DuplicateMemberException("Member is duplicate", ErrorCode.DUPLICATE_MEMBER);
        }
        String password = passwordEncoder.encode(memberDto.getPassword());
        Member member = memberDto.toEntity(password);
        return memberRepository.save(member).getId();
    }

    public Map<String, String> login(LoginDto loginDto){
        Member member = getMemberByEmail(loginDto.getEmail());
        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())){
            throw new PasswordNotCorrectException("Password isn't correct", ErrorCode.PASSWORD_NOT_CORRECT);
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
        Member member = getMemberByEmail(userEmail);
        member.updateRefreshToken(null);
    }

    public void withdrawal(){
        String userEmail = getUserEmail();
        Member member = getMemberByEmail(userEmail);
        memberRepository.delete(member);
    }

    public void updatePassword(PasswordChangeDto passwordChangeDto){
        String email = getUserEmail();
        Member member = getMemberByEmail(email);
        String password = passwordEncoder.encode(passwordChangeDto.getPassword());
        member.updatePassword(password);
    }

    public MemberResponseDto findOne(Long memberIdx){
        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new MemberNotFindException("Member can't find", ErrorCode.MEMBER_NOT_FIND));
        return MemberResponseDto.builder()
                .name(member.getName())
                .build();

    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFindException("Member can't find", ErrorCode.MEMBER_NOT_FIND));
    }

    private String sendEmail(String email){
        String key=createKey();

        EmailCertificateDto emailCertificateDto = new EmailCertificateDto();
        EmailCertificate emailCertificate = emailCertificateDto.toEntity(email, key);
        emailCertificateRepository.deleteByEmail(email);
        emailCertificateRepository.save(emailCertificate);

        return key;
    }

    private String createKey(){
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int num = 0;

        while(buffer.length() < 6) {
            num = random.nextInt(10);
            buffer.append(num);
        }

        return buffer.toString();
    }

    static public String getUserEmail() {
        String email = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            email = ((Member) principal).getEmail();
        } else{
            email = principal.toString();
        }
        return email;
    }
}
