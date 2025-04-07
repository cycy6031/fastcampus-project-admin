package com.fastcampus.projectboardadmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable);
        http
            .authorizeHttpRequests(
                authorize -> authorize
                    .anyRequest().permitAll()
            );
        http
            .formLogin( form -> form
                .loginPage("/"));
        http
            .logout((logout) -> logout
                    .logoutSuccessUrl("/"));
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

}
