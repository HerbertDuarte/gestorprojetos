package com.herbertduarte.gestorprojetos.config.security;

import com.herbertduarte.gestorprojetos.models.entities.Usuario;
import com.herbertduarte.gestorprojetos.repositories.IUsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioMockedRepository implements IUsuarioRepository {

    private final List<Usuario> usuarios = List.of(
            new Usuario(UUID.randomUUID().toString(),"admin", "$2a$12$FHInCPY/Q3tf.itrU9hvDONO15F4vhva2223fCd45RWl/YiYbiW6a")
    );

    public Optional<Usuario> findByUsername(String username){
        return usuarios.stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }
}
