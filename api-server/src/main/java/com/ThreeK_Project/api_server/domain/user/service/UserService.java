package com.ThreeK_Project.api_server.domain.user.service;

import static com.ThreeK_Project.api_server.domain.user.message.UserExceptionMessage.DUPLICATE_NAME;
import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.SIGN_UP_SUCCESS;
import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.UPDATE_USER_INFO_SUCCESS;

import com.ThreeK_Project.api_server.domain.user.dto.SignUpRequest;
import com.ThreeK_Project.api_server.domain.user.dto.UpdateUserInfoRequest;
import com.ThreeK_Project.api_server.domain.user.dto.UserInfoResponse;
import com.ThreeK_Project.api_server.domain.user.enums.Role;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.repository.UserRepository;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import jakarta.validation.Valid;
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
    public SuccessResponse signUp(SignUpRequest requestParam) {
        validateDuplicateName(requestParam.username());
        Role role = Role.fromValue(requestParam.role());
        String encodedPassword = passwordEncoder.encode(requestParam.password());

        saveUser(requestParam, encodedPassword, role);
        return new SuccessResponse(SIGN_UP_SUCCESS.getValue());
    }

    // 이름 중복 검증
    private void validateDuplicateName(String username){
        Optional<User> findMember = userRepository.findByUsername(username);
        if (findMember.isPresent()) {
            throw new ApplicationException(DUPLICATE_NAME.getValue());
        }
    }

    private void saveUser(SignUpRequest reqeustParam, String encodedPassword, Role role) {
        User user = User.createUser(reqeustParam.username(), encodedPassword, role, reqeustParam.phoneNumber(),
                reqeustParam.address());
        userRepository.save(user);
    }

    // 사용자 정보 조회
    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(User user) {
        return new UserInfoResponse(user);
    }

    // 사용자 정보 수정
    @Transactional
    public SuccessResponse updateUserInfo(User user, @Valid UpdateUserInfoRequest requestParam) {
        validateDuplicateName(requestParam.username());
        String encodedPassword = passwordEncoder.encode(requestParam.password());

        user.updateUserInfo(requestParam.username(), encodedPassword, requestParam.phoneNumber(),
                requestParam.address());
        return new SuccessResponse(UPDATE_USER_INFO_SUCCESS.getValue());
    }

}
