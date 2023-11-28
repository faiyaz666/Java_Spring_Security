package com.example.springsecurity.config;

import com.example.springsecurity.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class WebSecurity {
    private final CustomUserDetailService customUserDetailsService;

    public WebSecurity(CustomUserDetailService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headersConfigurer -> headersConfigurer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                .authorizeHttpRequests(authConfig -> authConfig
                        .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(antMatcher("/login/**")).permitAll()
                        .requestMatchers(antMatcher("/css/**")).permitAll()
                        .requestMatchers(antMatcher("/register/**")).permitAll()
                        .requestMatchers(antMatcher("/user/register/**")).permitAll()
                        .requestMatchers(antMatcher("/user/**")).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(antMatcher("/admin/**")).hasAuthority("ROLE_ADMIN")
                        .anyRequest().fullyAuthenticated())
                .formLogin(loginConfigurer -> loginConfigurer
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                )
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/logout")
                )
                .userDetailsService(customUserDetailsService);

        return httpSecurity.build();

    }
}
