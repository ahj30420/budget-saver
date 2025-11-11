package com.project.expensemanger.core.common.security.filter;

import com.project.expensemanger.core.common.exception.errorcode.AuthErrorCode;
import com.project.expensemanger.core.common.security.jwt.JwtProperties;
import com.project.expensemanger.core.common.security.jwt.JwtProvider;
import com.project.expensemanger.core.common.security.vo.CustomUserDetails;
import com.project.expensemanger.core.common.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final ResponseUtil responseUtil;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            setAuthenticationContext(request);
        } catch (ExpiredJwtException ee) {
            responseUtil.writeJsonErrorResponse(response, AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (SignatureException se) {
            responseUtil.writeJsonErrorResponse(response, AuthErrorCode.INVALID_SIGNATURE_ACCESS_TOKEN);
        } catch (JwtException je) {
            responseUtil.writeJsonErrorResponse(response, AuthErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !validAuthorizationHeader(request);
    }

    private boolean validAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authorizationHeader != null && authorizationHeader.startsWith(jwtProperties.getTokenPrefix());
    }

    private void setAuthenticationContext(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(createAuthenticatedToken(request));
    }

    private Authentication createAuthenticatedToken(HttpServletRequest request) {
        CustomUserDetails userDetails = createUserDetails(request);
        return new UsernamePasswordAuthenticationToken(userDetails.getUserIdx(), null, userDetails.getAuthorities());
    }

    private CustomUserDetails createUserDetails(HttpServletRequest request) {
        String token = getAuthenticationToken(request);
        return new CustomUserDetails(jwtProvider.getClaims(token));
    }

    private String getAuthenticationToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return header != null ? header.substring(jwtProperties.getTokenPrefix().length()) : null;
    }
}
