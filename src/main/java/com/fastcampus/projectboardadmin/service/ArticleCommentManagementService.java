package com.fastcampus.projectboardadmin.service;

import com.fastcampus.projectboardadmin.dto.ArticleCommentDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleCommentManagementService {

    public List<ArticleCommentDto> getArticleComments(){
        return List.of();
    }

    public ArticleCommentDto getArticleComment(Long articleCommentId){
        return null;
    }

    public void deleteArticleComment(Long articleCommentId){

    }
}
