package com.fastcampus.projectboardadmin.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fastcampus.projectboardadmin.config.SecurityConfig;
import com.fastcampus.projectboardadmin.config.TestSecurityConfig;
import com.fastcampus.projectboardadmin.dto.ArticleDto;
import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fastcampus.projectboardadmin.service.ArticleManagementService;
import com.fastcampus.projectboardadmin.service.UserAccountManagementService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트롤러 - 유저 관리")
@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = UserAccountManagementController.class)
class UserAccountManagementControllerTest {

    private final MockMvc mvc;

    @MockitoBean
    private UserAccountManagementService userAccountManagementService;

    public UserAccountManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 유저 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingUserAccountManagementView_thenReturnsUserAccountManagementView() throws Exception {
        // Given
        given(userAccountManagementService.getUserAccounts()).willReturn(List.of());

        // When & Then
        mvc.perform(get("/management/user-accounts"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("management/user-accounts"))
            .andExpect(model().attribute("userAccounts", List.of()));
        then(userAccountManagementService).should().getUserAccounts();

    }

    @DisplayName("[view][GET] 회원 관리 페이지 - 정상 호출")
    @Test
    void givenUserId_whenRequestingUserAccount_thenReturnsUserAccount() throws Exception {
        // Given
        String userId = "bomi";
        UserAccountDto userAccountDto = createUserAccountDto();
        given(userAccountManagementService.getUserAccount(userId)).willReturn(userAccountDto);

        // When & Then
        mvc.perform(get("/management/user-accounts/" + userId))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.userId").value(userId))
            .andExpect(jsonPath("$.email").value(userAccountDto.email()))
            .andExpect(jsonPath("$.nickname").value(userAccountDto.nickname()));
        then(userAccountManagementService).should().getUserAccount(userId);
    }

    @DisplayName("[view][POST] 회원 삭제 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingDeletion_thenRedirectsToArticleManagementView() throws Exception {
        // Given
        String userId = "bomi";
        willDoNothing().given(userAccountManagementService).deleteUserAccount(userId);

        // When & Then
        mvc.perform(post("/management/user-accounts/" + userId).with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("forward:/management/user-accounts"))
            .andExpect(forwardedUrl("/management/user-accounts"));
        then(userAccountManagementService).should().deleteUserAccount(userId);
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
            "bomiTest",
            "bomi@email.com",
            "bomi-test",
            "bomi memo"
        );
    }

}