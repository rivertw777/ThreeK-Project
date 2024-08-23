package com.ThreeK_Project.api_server.domain.user.service;

import static com.ThreeK_Project.api_server.domain.user.exception.UserExceptionMessage.DUPLICATE_NAME;

import com.ThreeK_Project.api_server.domain.user.dto.SignUpRequest;
import com.ThreeK_Project.api_server.domain.user.entity.Role;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.repository.UserRepository;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    @Transactional
    public void signUp(SignUpRequest reqeustParam) {
        validateDuplicateName(reqeustParam.username());
        String encodedPassword = passwordEncoder.encode(reqeustParam.password());
        Role role = Role.fromValue(reqeustParam.role());

        saveUser(reqeustParam.username(), encodedPassword, role);
    }

    // 이름 중복 검증
    private void validateDuplicateName(String username){
        Optional<User> findMember = userRepository.findByUsername(username);
        if (findMember.isPresent()) {
            throw new ApplicationException(DUPLICATE_NAME.getValue());
        }
    }

    private void saveUser(String username, String encodedPassword, Role role) {
        User user = User.createUser(username, encodedPassword, role);
        userRepository.save(user);
    }

}
