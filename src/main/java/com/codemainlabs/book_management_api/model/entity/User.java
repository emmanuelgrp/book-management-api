package com.codemainlabs.book_management_api.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private LocalDateTime lastLogin;

    private LocalDate createdAt;


//    private String role;                  // Rol del usuario
//    private boolean active;               // Estado de la cuenta (activo/inactivo)
//    private int failedLoginAttempts;      // Número de intentos fallidos de inicio de sesión
//    private String profileImageUrl;       // Imagen de perfil
}