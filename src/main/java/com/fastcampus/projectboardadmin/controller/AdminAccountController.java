package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.repository.AdminAccountRepository;
import com.fastcampus.projectboardadmin.service.AdminAccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/admin/members")
@Controller
public class AdminAccountController {

    @Autowired
    private AdminAccountService adminAccountService;

    @GetMapping
    public String members(Model model) {
        return "admin/members";
    }

    @ResponseBody
    @GetMapping("/api/admin/members")
    public List<AdminAccountResponse> getMembers(){
        return adminAccountService.users().stream()
            .map(AdminAccountResponse::from)
            .toList();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @DeleteMapping("/api/admin/members/{userId}")
    public void delete(@PathVariable String userId){
        adminAccountService.deleteUser(userId);
    }
}

