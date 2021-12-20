package com.account.controller;

import com.account.entity.Account;
import com.account.jwt.JwtTokenProvider;
import com.account.service.AccountService;
import com.account.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final JwtTokenProvider tokenProvider;
    private final AccountService accountService;
    private final UserService userService;


    // 1:성공 -1:로그인안됨/토큰만료됨  -2:다른사람거임  -3: 에러  0 : 형식 틀림

    //account status -   1 : 활성화, 0 : 삭제상태


    //가계부 등록 - 금액, 제목, 메모 입력.
    @PostMapping("/new")
    @ResponseBody
    public int newAccount(@RequestBody Account account, @CookieValue(value = "token",required = false)Cookie cookie){
        try{
            long userId = userService.userId(cookie);
            account.setUserId(userId);
            accountService.newAccount(account);

            return 1;
        }
        catch (Exception e){
            e.printStackTrace();
            return -3;
        }


    }


    //지우길/복구하길 원하는 account의 id
    @PostMapping("/deleteOrBack")
    @ResponseBody
    public int delete(@RequestBody Account account,@CookieValue(value = "token",required = false) Cookie cookie){
        try{
            long userId = userService.userId(cookie);
            account = accountService.oneAccount(account);
            if(account.getUserId() == userId) {

                switch (account.getStatus()){
                    case 1:
                        account.setStatus(0);
                        break;
                    case 0:
                        account.setStatus(1);
                        break;
                }

                account.setUpdatedAt(LocalDateTime.now());
                accountService.deleteOrBack(account);
            }else return -2;

            return 1;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return -1;
        }
        catch (Exception e){
            e.printStackTrace();
            return -3;
        }


    }


    //영구삭제
    @PostMapping("/RealDelete")
    @ResponseBody
    public int back(@RequestBody Account account,@CookieValue(value = "token",required = false) Cookie cookie){
        try{
            long userId = userService.userId(cookie);
            account = accountService.oneAccount(account);
            if(account.getUserId() == userId) {
                accountService.delete(account);
            }else return -2;


            return 1;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return -1;
        }
        catch (Exception e){
            e.printStackTrace();
            return -3;
        }
    }



}
