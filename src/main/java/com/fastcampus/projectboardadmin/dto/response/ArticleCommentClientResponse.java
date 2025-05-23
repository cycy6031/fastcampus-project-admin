package com.fastcampus.projectboardadmin.dto.response;

import com.fastcampus.projectboardadmin.dto.ArticleCommentDto;
import com.fastcampus.projectboardadmin.dto.ArticleDto;
import com.fastcampus.projectboardadmin.dto.response.ArticleClientResponse.Embedded;
import com.fastcampus.projectboardadmin.dto.response.ArticleClientResponse.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ArticleCommentClientResponse(
    @JsonProperty("_embedded") Embedded embedded,
    @JsonProperty("page") Page page
) {
    public static ArticleCommentClientResponse empty(){
        return new ArticleCommentClientResponse(
            new ArticleCommentClientResponse.Embedded(List.of()),
            new ArticleCommentClientResponse.Page(1, 0, 1, 0)
        );
    }

    public static ArticleCommentClientResponse of(List<ArticleCommentDto> articleComments){
        return new ArticleCommentClientResponse(
            new ArticleCommentClientResponse.Embedded(articleComments),
            new ArticleCommentClientResponse.Page(articleComments.size(), articleComments.size(), 1, 0)
        );
    }

    public List<ArticleCommentDto> articleComments() { return this.embedded.articleComments(); }

    public record Embedded(List<ArticleCommentDto> articleComments){}

    public record Page(
        int size,
        long totalElements,
        int totalPages,
        int number
    ) {}

}
