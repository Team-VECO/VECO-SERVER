package com.project.mt.dto.request;

import com.project.mt.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRequestDto {
    private String email;
    private String password;
    private String name;
    public Member toEntity(String password){
        return Member.builder()
                .email(this.email)
                .name(this.name)
                .password(password)
                .build();
    }
}
