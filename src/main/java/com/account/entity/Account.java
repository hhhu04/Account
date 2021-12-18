package com.account.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int price;

    private String title;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int status;  // 1:일반, 0:삭제상태





    ///////////////////

    public Account setAccount(Account account) {
        account.setStatus(1);
        account.setCreatedAt(LocalDateTime.now());
        return account;
    }
}
