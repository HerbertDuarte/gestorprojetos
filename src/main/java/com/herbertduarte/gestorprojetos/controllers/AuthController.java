package com.herbertduarte.gestorprojetos.controllers;


import com.herbertduarte.gestorprojetos.exceptions.globals.ErrorResponseDto;
import com.herbertduarte.gestorprojetos.security.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("login")
    @Operation(summary = "Autenticar usuário", description = "Realiza autenticação do usuário e retorna um token JWT válido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida", 
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Acesso Negado",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class),
                            examples = @ExampleObject(value = AcessoNegadoException.example)
                    )
            )
    })
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto payload){
        var userNamePassword = new UsernamePasswordAuthenticationToken(payload.username(), payload.password());
        var authentication = authenticationManager.authenticate(userNamePassword);
        UsuarioAutenticado usuarioAutenticado = Optional.of((UsuarioAutenticado) authentication.getPrincipal())
                .orElseThrow(AcessoNegadoException::new);

        String token = jwtService.generateToken(usuarioAutenticado);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
