package com.br.devdiaries.configuration;

import com.br.devdiaries.service.JwtService;
import com.br.devdiaries.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public SecurityConfig(JwtService jwtService, UsuarioService usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        jwtAuthenticationFilter.setJwtService(jwtService);
        jwtAuthenticationFilter.setUsuarioService(usuarioService);

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    // Permite acesso público ao endpoint de listar usuários
                    .requestMatchers("/usuarios").permitAll()
                    // Permite acesso público ao endpoint de login
                    .requestMatchers("/usuarios/login").permitAll()
                    .requestMatchers("/usuarios/{id}").permitAll()
                    .requestMatchers("/usuarios/verificar").permitAll()
                    .requestMatchers("/posts").permitAll()
                    .requestMatchers("/posts/titulo/{titulo}").permitAll()
                    .requestMatchers("/posts/{postId}/curtir").permitAll()
                    .requestMatchers("/posts/{postId}/descurtir").permitAll()
                    .requestMatchers("/posts/{id}").permitAll()
                    .requestMatchers("/comentarios").permitAll()
                    .requestMatchers("/comentarios/{id}/curtir").permitAll()
                    .requestMatchers("/comentarios/{id}/descurtir").permitAll()
                    .requestMatchers("/comentarios/{id}").permitAll()
                    // Permite acesso à documentação da API
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    // Qualquer outra requisição deve ser autenticada
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return usuarioService;
    }
}
