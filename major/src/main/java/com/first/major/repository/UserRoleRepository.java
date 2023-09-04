package com.first.major.repository;

import com.first.major.domain.User;
import com.first.major.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  List<UserRole> findUserRoleByUser(User user);
}
