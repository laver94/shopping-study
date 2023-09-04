package com.first.major.configuration.auth;

import com.first.major.domain.User;
import com.first.major.repository.UserRepository;
import com.first.major.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;
  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
    return new CustomUserDetail(user, userService);
  }
}