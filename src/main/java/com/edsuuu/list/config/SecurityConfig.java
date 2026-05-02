package com.edsuuu.list.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Filtro de Segurança Stateless (Para API REST)
     * Tudo que começa com /api/ cairá neste filtro.
     * @Order(1) define que este filtro tem precedência sobre o próximo.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**") // Aplica esse filtro apenas para endpoints /api/**
            .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF (comum para APIs Stateless)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Libera os endpoints de autenticação
                .anyRequest().authenticated() // Todos os endpoints em /api/** precisam de autenticação
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Não cria sessão (Stateless)
            .authenticationProvider(authenticationProvider) // Define nosso AuthenticationProvider
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro do JWT ANTES do filtro padrão do Spring
        
        return http.build();
    }

    /**
     * Filtro de Segurança Stateful (Para Aplicações Web padrão)
     * Tudo que NÃO começar com /api/ cairá neste filtro (devido ao @Order(2) implícito e sem securityMatcher).
     */
    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Desabilitado para simplificar, mas em produção Stateful deve estar ativado!
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // Cria sessão se necessário (Stateful)
            .authorizeHttpRequests(auth -> auth
                // Você pode liberar rotas aqui: .requestMatchers("/login", "/public/**").permitAll()
                .anyRequest().authenticated() // Qualquer outra rota exige autenticação
            )
            // .formLogin(withDefaults()) // Comentado para desativar a tela de login padrão
            .httpBasic(withDefaults()); // Mantém apenas autenticação via Header (Basic Auth) ou outra estratégia futura

        return http.build();
    }
}
