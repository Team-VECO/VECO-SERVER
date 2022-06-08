package com.project.mt.service;

import com.project.mt.configuration.security.jwt.JwtTokenProvider;
import com.project.mt.domain.EmailCertificate;
import com.project.mt.domain.Member;
import com.project.mt.dto.request.EmailCertificateDto;
import com.project.mt.dto.request.LoginDto;
import com.project.mt.dto.request.MemberRequestDto;
import com.project.mt.dto.request.PasswordDto;
import com.project.mt.dto.response.MemberResponseDto;
import com.project.mt.enums.Role;
import com.project.mt.exception.ErrorCode;
import com.project.mt.exception.exception.DuplicateMemberException;
import com.project.mt.exception.exception.MemberNotFindException;
import com.project.mt.exception.exception.PasswordNotCorrectException;
import com.project.mt.repository.EmailCertificateRepository;
import com.project.mt.repository.MemberRepository;
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

    public void updatePassword(PasswordDto passwordDto){
        String email = getUserEmail();
        Member member = getMemberByEmail(email);
        String password = passwordEncoder.encode(passwordDto.getPassword());
        member.updatePassword(password);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findOne(Long memberIdx){
        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new MemberNotFindException("Member can't find", ErrorCode.MEMBER_NOT_FIND));
        return MemberResponseDto.builder()
                .name(member.getName())
                .build();
    }

    public void certificatePassword(PasswordDto passwordDto){
        Member member = getMemberByEmail(getUserEmail());
        if(!passwordEncoder.matches(passwordDto.getPassword(), member.getPassword())){
            throw new PasswordNotCorrectException("Password isn't correct", ErrorCode.PASSWORD_NOT_CORRECT);
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFindException("Member can't find", ErrorCode.MEMBER_NOT_FIND));
    }

    private String sendEmail(String email){//메일 전송 기능 추가해야됨
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
