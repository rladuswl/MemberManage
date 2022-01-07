package com.example.membermanage.repository;

import com.example.membermanage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

// <Entity, Entity-id>
public interface UserRepository extends JpaRepository<com.example.membermanage.entity.User, Integer> {
    User findByUsername(String username);
}
