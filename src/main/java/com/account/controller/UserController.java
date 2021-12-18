package com.account.controller;

import com.account.dto.AccountDto;
import com.account.dto.LoginDto;
import com.account.entity.Account;
import com.account.entity.User;
import com.account.jwt.JwtTokenProvider;
import com.account.service.AccountService;
import com.account.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final JwtTokenProvider tokenProvider;

    // 1:성공 -1:아디중복 -2:아디없음/비번틀림 -3그 밖의 에러  0 : 형식 틀림


    //테스트용 토큰발급기
    @PostMapping("/testApi")
    @ResponseBody
    public Map<String,String> test(@RequestBody LoginDto loginDto, HttpServletResponse response){
        try{
            User user = userService.getUserInfo(loginDto.getEmail());
            String token = userService.createToken(user,loginDto);
            Cookie cookie = new Cookie("token",token);
            cookie.setPath("/");
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);
            Map<String, String> map = new HashMap<>();
            map.put("token",token);
            return map;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    //가입요청.
    @PostMapping("/join")
    @ResponseBody
    public int join(@RequestBody User user){

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

    //로그인 요청.
    @PostMapping("/login")
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


    //내가 기록한 가계부 호출 _ 삭제상태 제외.   가계부 내역이 없다면 빈 list , 다른 email의 자료를 요청시 null 반환
    @GetMapping("/myAccount/{email}")
    public List<AccountDto> accounts(@PathVariable(value = "email") String email, @CookieValue(value = "token",required = false)Cookie cookie){
        List<AccountDto> list = new ArrayList<>();
        try {
            String token = cookie.getValue();
            String email2 = tokenProvider.getUserPk(token);
            if(!email.equals(email2)) throw new IllegalArgumentException();

            long userId = userService.getUserId(email);
            list = accountService.myAccounts(userId);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }

    //내가 삭제한 목록
    @GetMapping("/myDelete/{email}")
    public List<AccountDto> deletes(@PathVariable(value = "email") String email, @CookieValue(value = "token",required = false)Cookie cookie){
        List<AccountDto> list = new ArrayList<>();
        try{
            String token = cookie.getValue();
            String email2 = tokenProvider.getUserPk(token);
            if(!email.equals(email2)) throw new IllegalArgumentException();

            long userId = userService.getUserId(email);
            list = accountService.myDeletes(userId);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }


    @GetMapping("/myAccount/{email}/{id}")
    public Account accountDetail(@PathVariable(value = "email") String email,@PathVariable(value = "id") long id,Account account,
                                 @CookieValue(value = "token",required = false)Cookie cookie){
        try {
            String token = cookie.getValue();
            String email2 = tokenProvider.getUserPk(token);
            if (!email.equals(email2)) throw new IllegalArgumentException();

            long userId = userService.getUserId(email);
            account = accountService.accountDetail(id);

        }catch (Exception e ) {
            e.printStackTrace();
            return null;
        }

        return account;
    }





}
