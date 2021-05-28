package com.kiok.demo.repo;

import com.kiok.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepos extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
