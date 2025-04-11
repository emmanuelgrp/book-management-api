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

    private String nationality;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books;

    private LocalDate birthDate;

    private LocalDate deathDate;

    private String city;

    private String biography;


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
