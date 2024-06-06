package org.PayMyBuddy.configuration;

import org.PayMyBuddy.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackages = "org.PayMyBuddy.repository")
public class SecurityConfig {

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher loginPageMatcher = new AntPathRequestMatcher("/login");
        RequestMatcher homePageMatcher = new AntPathRequestMatcher("/home");
        RequestMatcher transferPageMatcher = new AntPathRequestMatcher("/transfer");
        RequestMatcher contactPageMatcher = new AntPathRequestMatcher("/contact");
        RequestMatcher ConnectionPageMatcher = new AntPathRequestMatcher("/connection");
        RequestMatcher updateBalancePageMatcher = new AntPathRequestMatcher("/update-balance");
        RequestMatcher profilePageMatcher = new AntPathRequestMatcher("/profile");
        RequestMatcher registerPageMatcher = new AntPathRequestMatcher("/register");

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(loginPageMatcher).permitAll()
                        .requestMatchers(homePageMatcher).authenticated()
                        .requestMatchers(transferPageMatcher).authenticated()
                        .requestMatchers(contactPageMatcher).authenticated()
                        .requestMatchers(ConnectionPageMatcher).authenticated()
                        .requestMatchers(updateBalancePageMatcher).authenticated()
                        .requestMatchers(profilePageMatcher).authenticated()
                        .requestMatchers(registerPageMatcher).permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password").defaultSuccessUrl("/home", true))
                .rememberMe(rememberMeConfigurer -> rememberMeConfigurer
                        .userDetailsService(userDetailsService()))
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login"));

        return http.build();
    }
}