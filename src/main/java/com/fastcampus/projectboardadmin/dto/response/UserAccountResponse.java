package com.fastcampus.projectboardadmin.dto.response;

import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public record UserAccountResponse(
    String userId,
    String email,
    String nickname,
    String memo,
    LocalDateTime createdAt,
    String createdBy
) {

    public static UserAccountResponse of(String userId, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy) {
        return new UserAccountResponse(userId, email, nickname, memo, createdAt, createdBy);
    }

    public static UserAccountResponse from(UserAccountDto dto) {
        return UserAccountResponse.of(
            dto.userId(),
            dto.email(),
            dto.nickname(),
            dto.memo(),
            dto.createdAt(),
            dto.createdBy()
        );
    }

}