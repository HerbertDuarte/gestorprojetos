package com.herbertduarte.gestorprojetos.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // uuid para id do usuário é o mais adequado em questão de segurança
    @Column(unique = true)
    private String username;
    private String password;
}
