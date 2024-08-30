package com.br.devdiaries.service;

import com.br.devdiaries.model.Usuario;
import com.br.devdiaries.repository.IUsuario;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final IUsuario repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, Usuario> pendingUsers = new HashMap<>();

    public UsuarioService(IUsuario repository, EmailService emailService) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.emailService = emailService;
    }

    public List<Usuario> listarUsuario() {
        return repository.findAll();
    }
    
    public void enviarCodigoVerificacao(Usuario usuario) {
        String verificationCode = String.valueOf(new Random().nextInt(900000) + 100000);
        verificationCodes.put(usuario.getEmail(), verificationCode);
        pendingUsers.put(usuario.getEmail(), usuario);
        
        String assunto = "Verification Code - Dev Diaries";
        String mensagem = String.format("Hello %s,\n\nYour verification code is: %s\n\nThanks for sign up in our blog!",
                usuario.getNome(), verificationCode);
        
        emailService.enviarEmail(usuario.getEmail(), assunto, mensagem);
    }
    
    public boolean verificarCodigo(String email, String codigo) {
          String storedCode = verificationCodes.get(email);
    if (storedCode != null && storedCode.equals(codigo)) {
        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }
        
        Usuario usuario = pendingUsers.get(email);
        
        if (repository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }
        
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        repository.save(usuario);
        verificationCodes.remove(email);
        pendingUsers.remove(email);
        return true;
    }
    return false;
    }

    public Usuario editarUsuario(Usuario usuario) {
        String encoder = this.passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(encoder);
        return repository.save(usuario);
    }

    public Boolean excluirUsuario(Integer id) {
        repository.deleteById(id);
        return true;
    }

    public Boolean validarSenha(String email, String rawPassword) {
    Usuario usuario = repository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));
    
    return passwordEncoder.matches(rawPassword, usuario.getSenha());
}
    
    public void solicitarRedefinicaoSenha(String email) {
    Usuario usuario = repository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    // Gerar token de redefinição de senha
    String token = UUID.randomUUID().toString();
    usuario.setResetPasswordToken(token);
    usuario.setResetPasswordExpiration(LocalDateTime.now().plus(1, ChronoUnit.HOURS));
    repository.save(usuario);

    // Enviar email com o token
    String assunto = "Password Reset Request";
    String mensagem = String.format("Hello %s,\n\nTo reset your password, please click the following link: "
            + "http://devdiaries.com.br/reset-password?token=%s\n\nIf you didn't request this, please ignore this email.",
            usuario.getNome(), token);
    emailService.enviarEmail(usuario.getEmail(), assunto, mensagem);
}

public void redefinirSenha(String token, String novaSenha) {
    Usuario usuario = repository.findByResetPasswordToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid or expired password reset token."));

    // Verificar se o token não expirou
    if (usuario.getResetPasswordExpiration().isBefore(LocalDateTime.now())) {
        throw new IllegalArgumentException("Password reset token has expired.");
    }

    // Atualizar a senha
    usuario.setSenha(passwordEncoder.encode(novaSenha));
    usuario.setResetPasswordToken(null);
    usuario.setResetPasswordExpiration(null);
    repository.save(usuario);
}


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles("USER")
                .build();
    }
}
