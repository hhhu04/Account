package com.account.service;

import com.account.dto.AccountDto;
import com.account.entity.Account;
import com.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void newAccount(Account account) throws Exception{
        account = account.setAccount(account);
        accountRepository.save(account);
    }


        public List<AccountDto> myAccounts(long userId) {
        return accountRepository.findAccountsByUserIdAndStatus(userId,1);
    }

    public  List<AccountDto> myDeletes(long userId) {
        return accountRepository.findAccountsByUserIdAndStatus(userId,0);
    }


    public Account oneAccount(Account account) {
        return accountRepository.findById(account.getId()).get();
    }


    public void deleteOrBack(Account account) {
        accountRepository.save(account);
    }


    public void delete(Account account) {
        accountRepository.delete(account);
    }

    public Account accountDetail(long id,long userId) {
        return accountRepository.findByIdAndUserId(id,userId);
    }




}
