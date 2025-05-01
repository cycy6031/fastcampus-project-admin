package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.dto.response.ArticleResponse;
import com.fastcampus.projectboardadmin.service.ArticleManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/management/articles")
@Controller
public class ArticleManagementController {

    private final ArticleManagementService articleManagementService;

    @GetMapping
    public String articles(Model model) {
        model.addAttribute(
            "articles",
            articleManagementService.getArticles().stream().map(ArticleResponse::withoutContent).toList()
        );
        model.addAttribute("pageUrl", "/management/articles");
        return "management/articles";
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ArticleResponse getArticle(@PathVariable Long id){
        return ArticleResponse.withContent(articleManagementService.getArticle(id));
    }

    @PostMapping("/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleManagementService.deleteArticle(id);
        return "redirect:/management/articles";
    }
}
