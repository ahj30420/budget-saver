package com.project.expensemanger.manager.application.service;

import com.project.expensemanger.core.common.security.vo.CustomUserDetails;
import com.project.expensemanger.manager.adaptor.in.api.dto.request.SignupRequest;
import com.project.expensemanger.manager.application.port.in.UserUseCase;
import com.project.expensemanger.manager.application.port.out.UserPort;
import com.project.expensemanger.manager.domain.User.User;
import com.project.expensemanger.manager.domain.User.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase, UserDetailsService {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userPort.findByEmail(email);
        return CustomUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    @Override
    public void registerUser(SignupRequest request) {
        validateDuplicateEmail(request.email());

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .name(request.name())
                .role(UserRole.USER)
                .build();

        userPort.save(user);
    }

    private void validateDuplicateEmail(String email) {
        userPort.assertEmailNotExists(email);
    }
}
