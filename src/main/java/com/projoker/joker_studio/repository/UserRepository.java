package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    User findByPhone(Long phone);
}
