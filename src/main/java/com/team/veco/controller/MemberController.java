package com.team.veco.controller;

import com.team.veco.dto.request.LoginDto;
import com.team.veco.dto.request.MemberRequestDto;
import com.team.veco.dto.request.PasswordDto;
import com.team.veco.dto.response.MemberResponseDto;
import com.team.veco.response.ResponseService;
import com.team.veco.response.result.CommonResultResponse;
import com.team.veco.response.result.SingleResultResponse;
import com.team.veco.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/v1/member")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ResponseService responseService;

    @PostMapping("/join")
    public CommonResultResponse join(@RequestBody MemberRequestDto memberRequestDto){
        memberService.join(memberRequestDto);
        return responseService.getSuccessResult();
    }

    @PostMapping("/login")
    public SingleResultResponse<Map<String, String>> login(@RequestBody LoginDto loginDto){
        return responseService.getSingleResult(memberService.login(loginDto));
    }

    @PostMapping("/logout")
    public CommonResultResponse logout(){
        memberService.logout();
        return responseService.getSuccessResult();
    }

    @DeleteMapping()
    public CommonResultResponse withdrawal(){
        memberService.withdrawal();
        return responseService.getSuccessResult();
    }

    @PatchMapping("/password")
    public CommonResultResponse updatePassword(@RequestBody PasswordDto passwordDto){
        memberService.updatePassword(passwordDto);
        return responseService.getSuccessResult();
    }

    @GetMapping("/{memberIdx}")
    public SingleResultResponse<MemberResponseDto> findOne(@PathVariable Long memberIdx){
        return responseService.getSingleResult(memberService.findOne(memberIdx));
    }

    @PostMapping("/password/certificate")
    public CommonResultResponse certificatePassword(@RequestBody PasswordDto passwordDto){
        memberService.certificatePassword(passwordDto);
        return responseService.getSuccessResult();
    }
}
