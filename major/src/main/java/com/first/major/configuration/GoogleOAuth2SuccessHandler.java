package com.first.major.configuration;

import com.first.major.domain.Role;
import com.first.major.domain.User;
import com.first.major.domain.UserRole;
import com.first.major.repository.RoleRepository;
import com.first.major.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 사용하지 않습니다!!!
 */
//@Component
@RequiredArgsConstructor
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
    Map<String, Object> attributes = token.getPrincipal().getAttributes();
    String email = attributes.get("email").toString();
    if (userRepository.findUserByEmail(email).isPresent()) {

    } else {
      User user = User.builder()
              .firstName(attributes.get("given_name").toString())
              .lastName(attributes.get("family_name").toString())
              .email(email)
              .build();
      UserRole userRole = new UserRole();
      userRole.addUser(user);
      userRole.addRole(roleRepository.findById(2L).get());
//      List<Role> roles = new ArrayList<>();
//      roles.add(roleRepository.findById(2L).get());
      userRepository.save(user);
    }

    redirectStrategy.sendRedirect(request, response, "/");
  }
}
