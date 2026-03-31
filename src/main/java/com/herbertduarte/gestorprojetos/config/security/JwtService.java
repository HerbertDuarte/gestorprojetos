package com.herbertduarte.gestorprojetos.security;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder encoder;

    public JwtService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(UsuarioAutenticado usuario){
        Instant agora = Instant.now();
        long umaHoraEmSegundos = 60 * 60;
        Instant dataExpiracao = agora.plusSeconds(umaHoraEmSegundos);

        var claims = JwtClaimsSet.builder()
                .issuer("spring-boot-jwt")
                .issuedAt(dataExpiracao)
                .subject(usuario.getUsername())
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
