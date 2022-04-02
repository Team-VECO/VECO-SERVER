package com.team.veco.configuration.security.jwt;

import com.team.veco.configuration.security.auth.MyUserDetailsService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final MyUserDetailsService memberService;
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken=request.getHeader("Authorization");
        String refreshToken=request.getHeader("RefreshToken");
        if(accessToken!=null){
            String userEmail=accessTokenExtractEmail(accessToken);
            if(userEmail!=null) registerUserinfoInSecurityContext(userEmail, request);
        }
        filterChain.doFilter(request, response);
    }
    private String accessTokenExtractEmail(String accessToken){
        try{
            return tokenProvider.getEmail(accessToken);
        }catch(JwtException | IllegalArgumentException e){
            throw new RuntimeException();
        }
    }
    private void registerUserinfoInSecurityContext(String userEmail, HttpServletRequest req){
        try{
            UserDetails userDetails = memberService.loadUserByUsername(userEmail);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }catch (NullPointerException e){
            throw new RuntimeException();
        }
    }
}