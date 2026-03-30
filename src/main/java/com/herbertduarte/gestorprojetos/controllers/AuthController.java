package com.herbertduarte.gestorprojetos.controllers;


import com.herbertduarte.gestorprojetos.security.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto payload){
        var userNamePassword = new UsernamePasswordAuthenticationToken(payload.username(), payload.password());
        var authentication = authenticationManager.authenticate(userNamePassword);
        UsuarioAutenticado usuarioAutenticado = Optional.of((UsuarioAutenticado) authentication.getPrincipal())
                .orElseThrow(AcessoNegadoException::new);

        String token = jwtService.generateToken(usuarioAutenticado);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
