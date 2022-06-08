package com.project.mt.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mt.exception.ErrorCode;
import com.project.mt.exception.ErrorResponse;
import com.project.mt.exception.exception.AccessTokenExpiredException;
import com.project.mt.exception.exception.InvalidTokenException;
import com.project.mt.exception.exception.RefreshTokenExpiredException;
import com.project.mt.exception.exception.UserNotFoundException;
import org.apache.http.entity.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (InvalidTokenException e){
            responseErrorMessage(response, e.getErrorCode());
        } catch(AccessTokenExpiredException e){
            responseErrorMessage(response, e.getErrorCode());
        } catch(RefreshTokenExpiredException e){
            responseErrorMessage(response, e.getErrorCode());
        }catch (UserNotFoundException e){
            responseErrorMessage(response, e.getErrorCode());
        }catch(Exception e){
            log.error("알 수 없는 에러", e);
            responseErrorMessage(response, ErrorCode.UNKNOWN_ERROR);
        }
    }

    private void responseErrorMessage(HttpServletResponse response, ErrorCode errorCode){
        response.setStatus(errorCode.getStatus());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        try{
            ErrorResponse errorResponse = new ErrorResponse(errorCode);
            String errorResponseEntityToJson = objectMapper.writeValueAsString(errorResponse);

            response.getWriter().write(errorResponseEntityToJson);
        }catch(IOException e){
            log.error("Filter에서 Json변환 실패", e);
            throw new RuntimeException(e);
        }
    }
}

