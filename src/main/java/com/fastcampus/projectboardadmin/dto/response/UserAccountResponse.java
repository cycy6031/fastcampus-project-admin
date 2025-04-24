package com.fastcampus.projectboardadmin.dto.response;

import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record UserAccountResponse(
    @JsonProperty("_embedded") Embedded embedded,
    @JsonProperty("page") Page page
) {

    public static UserAccountResponse empty(){
        return new UserAccountResponse(
            new UserAccountResponse.Embedded(List.of()),
            new UserAccountResponse.Page(1, 0, 1, 0)
        );
    }

    public static UserAccountResponse of(List<UserAccountDto> userAccounts){
        return new UserAccountResponse(
            new UserAccountResponse.Embedded(userAccounts),
            new UserAccountResponse.Page(userAccounts.size(), userAccounts.size(), 1, 0)
        );
    }

    public List<UserAccountDto> userAccounts() { return this.embedded.userAccounts(); }

    public record Embedded(List<UserAccountDto> userAccounts){}

    public record Page(
        int size,
        long totalElements,
        int totalPages,
        int number
    ) {}
}
