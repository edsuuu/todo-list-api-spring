package com.edsuuu.list.controller;

import com.edsuuu.list.database.model.UserEntity;
import com.edsuuu.list.dto.AuthDTO;
import com.edsuuu.list.dto.TokenResponseDTO;
import com.edsuuu.list.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> authenticate(@Valid @RequestBody AuthDTO request) {
        
        // Verifica as credenciais. Se estiver errado, vai lançar um BadCredentialsException
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Se chegou aqui, a senha e o email estão corretos
        var user = (UserEntity) authentication.getPrincipal();
        
        // Gera o token
        var jwtToken = jwtService.generateToken(user);
        
        return ResponseEntity.ok(TokenResponseDTO.builder().token(jwtToken).build());
    }
}
