package com.fastcampus.projectboardadmin.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import com.fastcampus.projectboardadmin.dto.security.BoardAdminPrincipal;
import com.fastcampus.projectboardadmin.dto.security.KakaoOAuth2Response;
import com.fastcampus.projectboardadmin.service.AdminAccountService;
import jakarta.persistence.SecondaryTable;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] rolesAboveManager = {RoleType.MANAGER.name(), RoleType.DEVELOPER.name(), RoleType.ADMIN.name()};

        http
            .csrf(AbstractHttpConfigurer::disable);
        http
            .authorizeHttpRequests(
                authorize -> authorize
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/**")
                    ).hasAnyRole(rolesAboveManager)
                    .requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/**")
                    ).hasAnyRole(rolesAboveManager)
                    .anyRequest()
                    //.authenticated()
                    .permitAll()
            );
        http
            .formLogin(withDefaults());
        http
            .logout((logout) -> logout
                    .logoutSuccessUrl("/"));
        http.oauth2Login(withDefaults());
            /*.authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(
                    HttpMethod.GET,
                    "/articles",
                    "/articles/search-hashtag"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin().and()
            .logout()
            .logoutSuccessUrl("/")
            .and()
            .build();*/

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(AdminAccountService adminAccountService) {
        return username -> adminAccountService
            .searchUser(username)
            .map(BoardAdminPrincipal::from)
            .orElseThrow(() -> new UsernameNotFoundException("User not found : " + username));
    }

    /**
     * <p>
     *    Oauth 2.0 기술을 이용한 인증 정보를 처리한다.
     *    카카오 인증 방식을 선택
     *
     * @param adminAccountService 게시판 서비스의 사용자 계정을 다루는 서비스 로직
     * @param passwordEncoder 패스워드 암호화 도구
     * @return {@link OAuth2UserService} OAth2 인증 사용자 정보를 읽어들이고 처리하는 서비스 인스턴스 반환
     */
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
        AdminAccountService adminAccountService,
        PasswordEncoder passwordEncoder
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String providerId = String.valueOf(kakaoResponse.id());
            String username = registrationId + "_" + providerId;
            String dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID());
            Set<RoleType> roleTypes = Set.of(RoleType.USER);

            return adminAccountService.searchUser(username)
                .map(BoardAdminPrincipal::from)
                .orElseGet(() ->
                    BoardAdminPrincipal.from(
                        adminAccountService.saveUser(
                            username,
                            dummyPassword,
                            roleTypes,
                            kakaoResponse.email(),
                            kakaoResponse.nickname(),
                            null
                        )
                    )
                );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
