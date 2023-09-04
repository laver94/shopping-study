package com.first.major.repository;

import com.first.major.domain.Role;
import com.first.major.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByEmail(String email);

//  @Query("")
//  Role fetchJoin(User user);

}
