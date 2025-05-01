package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.dto.response.ArticleCommentResponse;
import com.fastcampus.projectboardadmin.service.ArticleCommentManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@RequestMapping("/management/article-comments")
@Controller
public class ArticleCommentManagementController {

    private final ArticleCommentManagementService articleCommentManagementService;

    @GetMapping
    public String articleComments(
        @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable
        , Model model
    ) {
        model.addAttribute(
            "comments",
            articleCommentManagementService.getArticleComments().stream().map(ArticleCommentResponse::of).toList()
        );
        model.addAttribute("pageUrl", "/management/article-comments");
        return "management/article-comments";
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ArticleCommentResponse getArticleComment(@PathVariable Long id){
        return ArticleCommentResponse.of(articleCommentManagementService.getArticleComment(id));
    }

    @PostMapping("/{id}")
    public String deleteArticleComment(@PathVariable Long id) {
        articleCommentManagementService.deleteArticleComment(id);
        return "redirect:/management/articles-comments";
    }

}
