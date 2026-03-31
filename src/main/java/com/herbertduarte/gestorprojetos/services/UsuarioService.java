package com.herbertduarte.gestorprojetos.services;

import com.herbertduarte.gestorprojetos.config.security.UsuarioAutenticado;
import com.herbertduarte.gestorprojetos.exceptions.UsuarioNaoEncontradoException;
import com.herbertduarte.gestorprojetos.repositories.IUsuarioRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUsername(username)
                .map(UsuarioAutenticado::new)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }
}
