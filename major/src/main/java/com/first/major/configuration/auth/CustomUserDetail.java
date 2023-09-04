package com.first.major.configuration.auth;

import com.first.major.domain.User;
import com.first.major.domain.UserRole;
import com.first.major.repository.UserRoleRepository;
import com.first.major.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetail extends User implements UserDetails, OAuth2User {

  private User user;
  private Map<String, Object> attributes;
  private UserService userService;

  public CustomUserDetail(User user, UserService userService) {
    this.user = user;
    this.userService = userService;
  }

  public CustomUserDetail(User user, Map<String , Object> attributes, UserService userService) {
    this.user = user;
    this.attributes = attributes;
    this.userService = userService;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<String> roleNames = userService.findRoleByUserId(user.getId());
    Collection<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(roleNames);
//    super.getUserRoles().forEach(userRole -> {
//      authorityList.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
//    });
    return authorityList;
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String getName() {
    return getFirstName();
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }
}

