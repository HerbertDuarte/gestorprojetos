package com.herbertduarte.gestorprojetos.config.security;

import com.herbertduarte.gestorprojetos.models.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class UsuarioAutenticado implements UserDetails {

    private final Usuario usuario;

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    @NullMarked
    public String getUsername() {
        return usuario.getUsername();
    }
}
