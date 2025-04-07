package com.codemainlabs.book_management_api.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String isbn;

    private String synopsis;

    @ManyToMany
    @JoinTable(
            name = "author_books",
            joinColumns = @JoinColumn(name = "book_id"), // Foreign key in the 'author_books' table pointing to 'Book'
            inverseJoinColumns = @JoinColumn(name = "author_id") // Foreign key in the 'author_books' table pointing to 'Author'
    )
    private List<Author> authors; // List of authors associated with this book


    private LocalDate publicationDate;

    //private BigDecimal price;

    private String genre;

    private Integer pageCount;

    private String editorial;

    //private boolean available;

}
