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
@ToString
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @ManyToMany(mappedBy = "authors") // 'authors' es el campo en Book que mapea esta relación
    private List<Book> books; // Lista de libros asociados con este autor

    private LocalDate birthDate;

    private String biography;

    private String nationality;

    private String city;

    private LocalDate deathDate;

    @Transient
    public int getBookCount() {
        return books != null ? books.size() : 0;
    }

    @Transient
    public boolean isAlive() {
        return deathDate == null;
    }

    @Transient
    public String getName() {
        return firstName + " " + lastName;
    }

}
