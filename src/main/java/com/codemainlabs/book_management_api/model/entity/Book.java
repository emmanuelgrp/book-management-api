package com.codemainlabs.book_management_api.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    private LocalDate publicationDate;

    private BigDecimal price;

    private String genre;

    private Integer pageCount;

    //private boolean available;

}
