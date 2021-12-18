package com.account.dto;

import java.time.LocalDateTime;

public interface AccountDto {

    long getId();

    String getTitle();

    int getPrice();

    LocalDateTime getCreatedAt();

}
