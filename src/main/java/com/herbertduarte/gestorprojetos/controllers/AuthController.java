package com.herbertduarte.gestorprojetos.controllers;


import com.herbertduarte.gestorprojetos.security.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("login")
    public String login(Authentication authentication){
        return jwtService.generateToken(authentication);
    }
}
