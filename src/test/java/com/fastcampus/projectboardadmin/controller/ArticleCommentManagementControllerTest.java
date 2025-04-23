package com.fastcampus.projectboardadmin.controller;

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
import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import com.fastcampus.projectboardadmin.dto.ArticleCommentDto;
import com.fastcampus.projectboardadmin.dto.ArticleDto;
import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fastcampus.projectboardadmin.service.ArticleCommentManagementService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("컨트롤러 - 댓글 관리")
@Import(SecurityConfig.class)
@WebMvcTest(controllers = ArticleCommentManagementController.class)
class ArticleCommentManagementControllerTest {

    private final MockMvc mvc;

    @MockitoBean private ArticleCommentManagementService articleCommentManagementService;

    public ArticleCommentManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 댓글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleCommentManagementView_thenReturnsArticleCommentManagementView() throws Exception {
        // Given
        given(articleCommentManagementService.getArticleComments()).willReturn(List.of());

        // When & Then
        mvc.perform(get("/management/article-comments"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("management/article-comments"))
            .andExpect(model().attribute("comments", List.of()));
        then(articleCommentManagementService).should().getArticleComments();

    }

    @DisplayName("[view][GET] 댓글 관리 페이지 - 정상 호출")
    @Test
    void givenCommentId_whenRequestingComment_thenReturnsComment() throws Exception {
        // Given
        Long articleCommentId = 1L;
        ArticleCommentDto articleCommentDto = createArticleCommentDto("comment");
        given(articleCommentManagementService.getArticleComment(articleCommentId)).willReturn(articleCommentDto);

        // When & Then
        mvc.perform(get("/management/article-comments/" + articleCommentId))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(articleCommentId))
            .andExpect(jsonPath("$.articleId").value(articleCommentDto.articleId()))
            .andExpect(jsonPath("$.content").value(articleCommentDto.content()))
            .andExpect(jsonPath("$.userAccount.nickname").value(articleCommentDto.userAccount().nickname()));
        then(articleCommentManagementService).should().getArticleComment(articleCommentId);
    }

    @DisplayName("[view][POST] 댓글 삭제 - 정상 호출")
    @Test
    void givenCommentId_whenRequestingDeletion_thenRedirectsToArticleManagementView() throws Exception {
        // Given
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentManagementService).deleteArticleComment(articleCommentId);

        // When & Then
        mvc.perform(post("/management/article-comments/" + articleCommentId).with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("forward:/management/articles"))
            .andExpect(forwardedUrl("/management/articles"));
        then(articleCommentManagementService).should().deleteArticleComment(articleCommentId);
    }

    private ArticleCommentDto createArticleCommentDto(String comment) {
        return ArticleCommentDto.of(
            1L,
            1L,
            createUserAccountDto(),
            null,
            comment,
            LocalDateTime.now(),
            "bomi",
            LocalDateTime.now(),
            "bomi"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
            "bomiTest",
            "pw",
            Set.of(RoleType.ADMIN),
            "bomi@email.com",
            "bomi-test",
            "bomi memo"
        );
    }

}