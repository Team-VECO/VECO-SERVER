package com.project.mt.configuration.security.jwt;

import com.project.mt.configuration.security.auth.MyUserDetailsService;
import com.project.mt.enums.Role;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    public static long TOKEN_VALIDATION_SECOND = 1000L * 60 * 60 * 3;  // 3시간을 accessToken 만료 기간으로 잡는다
    public static long REFRESH_TOKEN_VALIDATION_SECOND = TOKEN_VALIDATION_SECOND * 24 * 180; // 6달을 refreshToken 만료 기간으로 잡는다.

    private final MyUserDetailsService myMemberDetails;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email, Role role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("auth", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDATION_SECOND);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String createRefreshToken(){
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_VALIDATION_SECOND);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = myMemberDetails.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }else{
            return null;
        }
    }

    public String resolveRefreshToken(HttpServletRequest req){
        String refreshToken = req.getHeader("RefreshToken");
        if(refreshToken != null && refreshToken.startsWith("Bearer ")){
            return refreshToken.substring(7);
        } else {
            return null;
        }
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException, IllegalArgumentException, MalformedJwtException, SignatureException, UnsupportedJwtException, PrematureJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
