package com.codemainlabs.book_management_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books;

    private LocalDate birthDate;          // Fecha de nacimiento

    private String biography;             // Biografía del autor

    private String nationality;           // Nacionalidad

    private LocalDate deathDate;          // Fecha de fallecimiento (si aplica)

    @Transient
    public int getBookCount() {          // Número de libros escritos (calculado)
        return books != null ? books.size() : 0;
    }
}
