package com.ThreeK_Project.api_server.global.security.auth;

import static com.ThreeK_Project.api_server.domain.user.message.UserExceptionMessage.USER_NOT_FOUND;

import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.repository.UserCacheRepository;
import com.ThreeK_Project.api_server.domain.user.repository.UserRepository;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import com.ThreeK_Project.api_server.global.security.jwt.TokenManager;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceCustom implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenManager tokenManager;
    private final UserCacheRepository userCacheRepository;

    // 이름으로 회원 조회
    @Override
    public UserDetailsCustom loadUserByUsername(String username) {
        User user = getUserFromCache(username);
        return new UserDetailsCustom(user);
    }

    private User getUserFromCache(String username) {
        User user = userCacheRepository.getUserCache(username);
        if (user == null) {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND.getValue()));
            userCacheRepository.setUserCache(username, user);
        }
        return user;
    }

    // 인증 정보 반환
    public Authentication extractAuthentication(String token) {
        Claims claims = tokenManager.parseClaims(token);
        String username = claims.getSubject();
        UserDetailsCustom userDetails = loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
