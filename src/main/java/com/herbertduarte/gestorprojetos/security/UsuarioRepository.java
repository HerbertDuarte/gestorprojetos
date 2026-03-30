package com.herbertduarte.gestorprojetos.security;

import com.herbertduarte.gestorprojetos.models.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository implements IUsuarioRepository {

    private final List<Usuario> usuarios = List.of(
            new Usuario(1,"admin", "admin")
    );

    public Optional<Usuario> findByUsername(String username){
        return usuarios.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }
}
