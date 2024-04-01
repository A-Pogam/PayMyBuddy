package org.PayMyBuddy.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "org.PayMyBuddy.repository")
public class SecurityConfig {

    /*fix pb missing url driver*/
    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/paymybuddy_db")
                .username("root")
                .password("rootroot")
                .build();
    }
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
                        .defaultSuccessUrl("/home", true) // Redirection après une connexion réussie
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                );
        return http.build();
    }
}

