package com.med.aftas.serverside.config;

import com.med.aftas.serverside.jwt.JWTAuthenticationFilter;
import com.med.aftas.serverside.service.impl.UserDetailsServiceImpl;
import com.med.aftas.serverside.util.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(getOpenedResources()).permitAll()
//                                .requestMatchers("api/v1/user/**").hasAuthority("ROLE_MANAGER")
                                .requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .userDetailsService(userDetailsService)
                .exceptionHandling(e -> e.accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    private String[] getOpenedResources() {
        return new String[]{
                "/h2-console/**",
                "/swagger-ui/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/v3/api-docs",
                "/v3/api-docs/**"
        };
    }
}
