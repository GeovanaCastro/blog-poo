package com.br.devdiaries.configuration;

import com.br.devdiaries.service.JwtService;
import com.br.devdiaries.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
        throws ServletException, IOException {
    
    // Ignorar o filtro para a URL de login
    if ("/usuarios/login".equals(request.getServletPath())) {
        filterChain.doFilter(request, response);
        return;
    }

    final String authorizationHeader = request.getHeader("Authorization");
    
    String username = null;
    String jwt = null;
    
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);
    }
    
    // Verificar se o token está revogado antes de continuar a validação
    if (jwt != null && usuarioService.isTokenRevoked(jwt)) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Token is revoked");
        return;
    }
    
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.usuarioService.loadUserByUsername(username);
        
        if (jwtService.validateToken(jwt, userDetails.getUsername())) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
    
    filterChain.doFilter(request, response);
    }
}


