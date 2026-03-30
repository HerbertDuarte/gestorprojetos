package com.herbertduarte.gestorprojetos.security;

import com.herbertduarte.gestorprojetos.models.Usuario;

import java.util.Optional;

public interface IUsuarioRepository {

    public  Optional<Usuario> findByUsername(String username);
}
