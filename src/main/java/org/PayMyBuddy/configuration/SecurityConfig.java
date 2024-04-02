package org.PayMyBuddy.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackages = "org.PayMyBuddy.repository")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher loginPageMatcher = new AntPathRequestMatcher("/login");
        RequestMatcher homePageMatcher = new AntPathRequestMatcher("/home");

        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(loginPageMatcher).permitAll()
                        .requestMatchers(homePageMatcher).authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true) // Redirection after a successful connection
                        .successHandler((request, response, authentication) -> {
                            // Log message to check if redirection to /home is triggered
                            System.out.println("Redirecting to /home after successful login");
                            response.sendRedirect("/home");
                        })

                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                );
        return http.build();
    }

}

