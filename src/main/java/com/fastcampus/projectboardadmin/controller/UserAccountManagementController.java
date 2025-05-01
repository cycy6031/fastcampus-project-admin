package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.dto.response.ArticleResponse;
import com.fastcampus.projectboardadmin.dto.response.UserAccountResponse;
import com.fastcampus.projectboardadmin.service.ArticleManagementService;
import com.fastcampus.projectboardadmin.service.UserAccountManagementService;
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
@RequestMapping("/management/user-accounts")
@Controller
public class UserAccountManagementController {

    private final UserAccountManagementService userAccountManagementService;

    @GetMapping
    public String userAccounts(
        @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable
        , Model model
    ) {
        model.addAttribute(
            "userAccounts",
            userAccountManagementService.getUserAccounts().stream().map(UserAccountResponse::from).toList()
        );
        model.addAttribute("pageUrl", "/management/user-accounts");
        return "management/user-accounts";
    }

    @ResponseBody
    @GetMapping("/{id}")
    public UserAccountResponse getArticle(@PathVariable String userId){
        return UserAccountResponse.from(userAccountManagementService.getUserAccount(userId));
    }

    @PostMapping("/{id}")
    public String deleteArticle(@PathVariable String userId) {
        userAccountManagementService.deleteUserAccount(userId);
        return "redirect:/management/user-accounts";
    }

}
