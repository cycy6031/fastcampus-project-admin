package com.fastcampus.projectboardadmin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import com.fastcampus.projectboardadmin.dto.ArticleDto;
import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fastcampus.projectboardadmin.dto.properties.ArticleClientResponse;
import com.fastcampus.projectboardadmin.dto.properties.ProjectProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@DisplayName("비지니스 로직 - 게시글 관리")
class ArticleManagementServiceTest {

    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleManagementService.class)
    @Nested
    class RestTemplateTest{

        private final ArticleManagementService sut;

        private final ProjectProperties projectProperties;
        private final MockRestServiceServer server;
        private final ObjectMapper mapper;

        @Autowired
        public RestTemplateTest(ArticleManagementService sut, ProjectProperties projectProperties,
            MockRestServiceServer server, ObjectMapper mapper) {
            this.sut = sut;
            this.projectProperties = projectProperties;
            this.server = server;
            this.mapper = mapper;
        }

        @DisplayName("게시글 목록 API를 호출하면, 게시글들을 가져온다.")
        void givenNothing_whenCallingArticlesApi_thenReturnsArticleList() throws Exception{
            // Given
            ArticleDto expectedArticle = createArticleDto("제목", "글");
            ArticleClientResponse expectedResponse = ArticleClientResponse.of(List.of(expectedArticle));
            server
                .expect(requestTo(projectProperties.board().url() + "/api/articles?size=10000"))
                .andRespond(withSuccess(
                    mapper.writeValueAsString(expectedResponse),
                    MediaType.APPLICATION_JSON
                ));

            // When
            List<ArticleDto> result = sut.getArticles();

            // Then
            assertThat(result).isNotEmpty();
        }

        private ArticleDto createArticleDto(String title, String content) {
            return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
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
  
}