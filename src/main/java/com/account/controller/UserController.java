package com.account.controller;

import com.account.dto.LoginDto;
import com.account.entity.User;
import com.account.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1:성공 -1:아디중복 -2:아디없음/비번틀림 -3그 밖의 에러  0 : 형식 틀림

    @PostMapping("user/join")
    @ResponseBody
    public int join(@RequestBody User user, @CookieValue(value = "token",required = false) Cookie cookie){

        try {

//            if(!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) return 0;
            if(!userService.checkUser(user.getEmail())){
                userService.join(user);

            }else{
                return -1;
            }

            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -3;
        }
    }

    @PostMapping("/user/login")
    @ResponseBody
    public int login(@RequestBody LoginDto loginDto, User user, HttpServletResponse response){
        try{
            if(!userService.checkUser(loginDto.getEmail())) return -2;
            user = userService.getUserInfo(loginDto.getEmail());
            String token = userService.createToken(user,loginDto);

            Cookie cookie = new Cookie("token",token);
            cookie.setPath("/");
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);

            return 1;
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            return -2;
        }
        catch (Exception e){
            e.printStackTrace();
            return -3;
        }

    }



}
