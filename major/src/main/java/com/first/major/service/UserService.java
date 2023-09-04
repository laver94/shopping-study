package com.first.major.service;

import com.first.major.domain.Role;
import com.first.major.domain.User;
import com.first.major.domain.UserRole;
import com.first.major.repository.UserRepository;
import com.first.major.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;

  // user로 role 배열 찾기
  public List<String> findRoleByUserId(Long id) {
    User user = userRepository.findById(id).orElseThrow();
    List<UserRole> userRoles = userRoleRepository.findUserRoleByUser(user);
    List<String> roleNames = userRoles.stream()
            .map(userRole -> userRole.getRole().getName())
            .collect(Collectors.toList());

    return roleNames;
  }


}
