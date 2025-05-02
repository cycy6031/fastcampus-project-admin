package com.fastcampus.projectboardadmin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fastcampus.projectboardadmin.config.SecurityConfig;
import com.fastcampus.projectboardadmin.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("View 컨트롤러 - 메인")
@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = MainController.class)
class MainControllerTest {

    private final MockMvc mvc;

    public MainControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[view][GET] 루트 페이지 -> 게시글 관리 페이지 Forwarding")
    @Test
    void givenNothing_whenRequestingRootView_thenForwardsToArticleManagementView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("forward:/management/articles"))
            .andExpect(forwardedUrl("/management/articles"));

    }

}