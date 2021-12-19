package com.account.service;

import com.account.dto.LoginDto;
import com.account.entity.Account;
import com.account.entity.User;
import com.account.jwt.JwtTokenProvider;
import com.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public boolean checkUser(String email){
        return userRepository.existsByEmail(email);
    }

    public void join(User user) {
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


    public long getUserId(String email) {
        return userRepository.findByEmail(email).get().getId();
    }

    public long userId(Cookie cookie){
        String token = cookie.getValue();
        String email = tokenProvider.getUserPk(token);
        return this.getUserId(email);
    }

    public String email2(Cookie cookie){
        String token = cookie.getValue();
        return tokenProvider.getUserPk(token);
    }


}
