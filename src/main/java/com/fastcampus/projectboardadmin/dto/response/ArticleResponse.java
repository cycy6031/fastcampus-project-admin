package com.fastcampus.projectboardadmin.dto.response;

import com.fastcampus.projectboardadmin.dto.ArticleDto;
import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArticleResponse(
    Long id,
    UserAccountDto userAccount,
    String title,
    String contnent,
    LocalDateTime createdAt
) {
    public static ArticleResponse of(Long id, UserAccountDto userAccount, String title, String content, LocalDateTime createdAt) {
        return new ArticleResponse(id, userAccount, title, content, createdAt);
    }

    public static ArticleResponse withContent(ArticleDto articleDto) {
        return new ArticleResponse(
            articleDto.id(),
            articleDto.userAccount(),
            articleDto.title(),
            articleDto.content(),
            articleDto.createdAt()
        );
    }

    public static ArticleResponse withoutContent(ArticleDto articleDto){
        return new ArticleResponse(
            articleDto.id(),
            articleDto.userAccount(),
            articleDto.title(),
            null,
            articleDto.createdAt()
        );
    }
}
