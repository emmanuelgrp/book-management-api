package com.codemainlabs.book_management_api.repository;

import com.codemainlabs.book_management_api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
