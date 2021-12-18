package com.account.repository;

import com.account.dto.AccountDto;
import com.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


    List<Account> findByUserIdAndStatus(Long userId, int status);


    List<AccountDto> findAccountsByUserIdAndStatus(Long userId, int status);


}
