package com.codemainlabs.book_management_api.repository;

import com.codemainlabs.book_management_api.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Modifying
    @Query(value = "DELETE FROM author_books WHERE author_id = :authorId", nativeQuery = true)
    void deleteBookAssociations(Long authorId);
}

