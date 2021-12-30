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

import javax.security.auth.login.LoginException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user",produces="application/json; charset=utf-8")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final AccountService accountService;
    private final JwtTokenProvider tokenProvider;

    // 1:성공 -1:아디중복 -2:아디없음/비번틀림 -3그 밖의 에러  0 : 형식 틀림




    //가입요청.  -email,password
    @PostMapping("/join")
    public int join(@RequestBody User user){

        try {

            if(!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) return 0;
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
    public Map<Integer,String> login(@RequestBody LoginDto loginDto, User user, HttpServletResponse response){
        Map<Integer,String> map=new HashMap<>();
        try{
            if(!userService.checkUser(loginDto.getEmail())) {
                map.put(-2,"null");
            }
            System.out.println(loginDto);
            user = userService.getUserInfo(loginDto.getEmail());
            String token = userService.createToken(user,loginDto);

            Cookie cookie = new Cookie("token",token);
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);
            map.put(1,token);
//            return 1;
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            map.put(-2,null);
//            return -2;
        }
        catch (Exception e){
            e.printStackTrace();
            map.put(-3,null);
//            return -3;
        }
        return map;
    }

    //로그아웃
    @GetMapping("/logout")
    public int logout(@CookieValue(value = "token",required = false)Cookie cookie, HttpServletResponse response){
       try {
           Cookie cookie1 = new Cookie("token", "");
           cookie1.setMaxAge(0);
           response.addCookie(cookie1);
           return 1;

       }catch (Exception e){
           e.printStackTrace();
       }
        return -1;
    }


    //내가 기록한 가계부 호출 _ 삭제상태 제외.   가계부 내역이 없다면 빈 list , 다른 email의 자료를 요청시 null 반환
    @GetMapping("/myAccount/{email}")
    public Map<Integer,List<AccountDto>> accounts(@PathVariable(name = "email") String email, @CookieValue(value = "token",required = false)Cookie cookie){
        Map<Integer, List<AccountDto>> map = new HashMap<>();
        List<AccountDto> list = new ArrayList<>();
        try {
            String email2 = userService.email2(cookie);
            if(!email.equals(email2)) throw new IllegalArgumentException();

            long userId = userService.getUserId(email);
            list = accountService.myAccounts(userId);
            map.put(1,list);
        }
        catch (Exception e){
            e.printStackTrace();
            map.put(-3,list);
        }
        return map;
    }

    //내가 삭제한 목록
    @GetMapping("/myDelete/{email}")
    public Map<Integer,List<AccountDto>> deletes(@PathVariable(name = "email") String email, @CookieValue(value = "token",required = false)Cookie cookie){
        Map<Integer, List<AccountDto>> map = new HashMap<>();
        List<AccountDto> list = new ArrayList<>();
        try{
            String email2 = userService.email2(cookie);
            if(!email.equals(email2)) throw new IllegalArgumentException();

            long userId = userService.getUserId(email);
            list = accountService.myDeletes(userId);
            map.put(1,list);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            map.put(-2,list);
        }
        catch (Exception e){
            e.printStackTrace();
            map.put(-3,list);
        }
        return map;
    }


    //해당 기록 자세히보기.
    @GetMapping("/myAccount/{email}/{id}")
    public Map<Integer,Account> accountDetail(@PathVariable(name = "email") String email,@PathVariable(value = "id") long id,Account account,
                                 @CookieValue(value = "token",required = false)Cookie cookie){
        Map<Integer, Account> map = new HashMap<>();
        try {
            String email2 = userService.email2(cookie);
            if (!email.equals(email2)) throw new IllegalArgumentException();

            long userId = userService.getUserId(email);
            account = accountService.accountDetail(id,userId);
            if(account == null) throw new IllegalArgumentException();
            map.put(1, account);
        }
       catch (IllegalArgumentException e){
                e.printStackTrace();
                map.put(-2,account);
            }
        catch (Exception e){
                e.printStackTrace();
                map.put(-3,account);
            }

        return map;
    }




}
