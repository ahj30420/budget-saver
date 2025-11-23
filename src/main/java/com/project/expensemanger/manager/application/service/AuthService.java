package com.project.expensemanger.manager.application.service;

import com.project.expensemanger.core.common.exception.BaseException;
import com.project.expensemanger.core.common.exception.errorcode.AuthErrorCode;
import com.project.expensemanger.core.common.security.jwt.JwtProvider;
import com.project.expensemanger.core.common.util.CookieUtils;
import com.project.expensemanger.core.common.util.ResponseUtil;
import com.project.expensemanger.manager.application.port.in.AuthUseCase;
import com.project.expensemanger.manager.application.port.out.AuthPort;
import com.project.expensemanger.manager.application.port.out.UserPort;
import com.project.expensemanger.manager.domain.User.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AuthPort authPort;
    private final UserPort userPort;
    private final CookieUtils cookieUtils;
    private final JwtProvider jwtProvider;
    private final ResponseUtil responseUtil;

    @Override
    public void registerRefreshToken(Long userId, String refreshToken) {
        authPort.saveRefreshToken(userId, refreshToken);
    }

    @Override
    public void delete(Long userId) {
        authPort.deleteRefreshToken(userId);
    }

    @Override
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        try {
            String preToken = cookieUtils.extractRefreshToken(request);

            if (preToken == null) {
                throw new BaseException(AuthErrorCode.INVALID_REFRESH_TOKEN);
            }

            User user = getUserBySubject(preToken);

            verifyRefreshToken(user, preToken);

            String newRefresh = generateRefreshToken(user.getEmail(), user.getId());
            String newAccess = generateAccessToken(user);

            updateRefreshToken(user, newRefresh);

            responseUtil.addTokensToResponse(response, newAccess, newRefresh);
        } catch (ExpiredJwtException ee) {
            throw new BaseException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (SignatureException se) {
            throw new BaseException(AuthErrorCode.INVALID_SIGNATURE_ACCESS_TOKEN);
        } catch (JwtException je) {
            throw new BaseException(AuthErrorCode.UNAUTHENTICATED);
        }
    }

    private User getUserBySubject(String refreshToken) {
        String email = findUserInfo(refreshToken);
        return userPort.findByEmail(email);
    }

    private String findUserInfo(String refreshToken) {
        return jwtProvider.getClaims(refreshToken).getSubject();
    }

    private void verifyRefreshToken(User user, String preToken) {
        String refreshToken = authPort.getRefreshToken(user.getId());
        if (!refreshToken.equals(preToken)) {
            throw new BaseException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private String generateRefreshToken(String subject, Long userId) {
        return jwtProvider.generateRefreshToken(subject, userId);
    }

    private String generateAccessToken(User user) {
        List<GrantedAuthority> authorities = mapToAuthorities(user);
        return jwtProvider.generateAccessToken(
                user.getEmail(),
                user.getId(),
                authorities
        );
    }

    private List<GrantedAuthority> mapToAuthorities(User user) {
        return List.of((GrantedAuthority) () -> "ROLE_" + user.getRole().name());
    }

    private void updateRefreshToken(User user, String newRefresh) {
        authPort.saveRefreshToken(user.getId(), newRefresh);
    }

}
