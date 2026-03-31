package com.herbertduarte.gestorprojetos.repositories;

import com.herbertduarte.gestorprojetos.models.entities.Usuario;

import java.util.Optional;

public interface IUsuarioRepository {

    public  Optional<Usuario> findByUsername(String username);
}
