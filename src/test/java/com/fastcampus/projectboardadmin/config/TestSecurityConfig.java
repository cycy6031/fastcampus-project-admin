package com.fastcampus.projectboardadmin.config;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import com.fastcampus.projectboardadmin.dto.AdminAccountDto;
import com.fastcampus.projectboardadmin.service.AdminAccountService;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

@Import(SecurityConfig.class)
@TestConfiguration
public class TestSecurityConfig {

    @MockitoBean private AdminAccountService adminAccountService;

    @BeforeTestMethod
    public void securitySetup(){
        given(adminAccountService.searchUser(anyString()))
            .willReturn(Optional.of(createAdminAccountDto()));
        given(adminAccountService.saveUser(anyString(), anyString(), anySet(), anyString(), anyString(), anyString()))
            .willReturn(createAdminAccountDto());
    }

    private AdminAccountDto createAdminAccountDto(){
        return AdminAccountDto.of(
            "bomiTest",
            "pw",
            Set.of(RoleType.USER),
            "bomi@email.com",
            "bomitest",
            "bomiMemo"
        );
    }
}
