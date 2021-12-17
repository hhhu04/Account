package com.account.service;

import com.account.dto.LoginDto;
import com.account.entity.User;
import com.account.jwt.JwtTokenProvider;
import com.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public boolean checkUser(String email){
        return userRepository.existsByEmail(email);
    }

    public void join(User user) throws Exception{
        user = user.setUser(user,passwordEncoder);
        userRepository.save(user);
    }

    public User getUserInfo(String email) {
        return userRepository.getUserByEmail(email);
    }

    public String createToken(User user, LoginDto loginDto) {
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) throw new IllegalArgumentException();
        return tokenProvider.createToken(user.getEmail(),user.getRole());
    }
}
