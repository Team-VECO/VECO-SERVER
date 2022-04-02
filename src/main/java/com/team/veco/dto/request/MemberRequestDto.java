package com.team.veco.dto.request;

import com.team.veco.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
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
