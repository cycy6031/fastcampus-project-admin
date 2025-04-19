package com.fastcampus.projectboardadmin.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/management/articles")
@Controller
public class ArticleManagementController {

    @GetMapping
    public String articles(
        @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable
        , Model model
    ) {
        model.addAttribute("pageUrl", "/management/articles");
        return "management/articles";
    }

}
