package com.account.repository;

import com.account.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void create(){
        Account account = new Account();
        account.setUserId(1L);
        account.setPrice(5000);
        account.setNote("테스트");
        account.setCreatedAt(LocalDateTime.now());
        account.setStatus(1);
        accountRepository.save(account);

    }

}