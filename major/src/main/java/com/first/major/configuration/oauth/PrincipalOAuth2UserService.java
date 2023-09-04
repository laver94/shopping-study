package com.first.major.configuration.oauth;

import com.first.major.configuration.auth.CustomUserDetail;
import com.first.major.configuration.oauth.provider.GoogleUserInfo;
import com.first.major.configuration.oauth.provider.OAuth2UserInfo;
import com.first.major.domain.User;
import com.first.major.domain.UserRole;
import com.first.major.repository.RoleRepository;
import com.first.major.repository.UserRepository;
import com.first.major.repository.UserRoleRepository;
import com.first.major.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserRoleRepository userRoleRepository;
  private final UserService userService;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);

    OAuth2UserInfo oAuth2UserInfo = null;
    if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
      oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
    }
    String email = oAuth2UserInfo.getEmail();
    Optional<User> findUser = userRepository.findUserByEmail(email);
    if (findUser.isEmpty()) {
      User user = User.builder()
              .email(email)
              .firstName(oAuth2UserInfo.getFirstName())
              .lastName(oAuth2UserInfo.getLastName())
              .password(bCryptPasswordEncoder.encode("chang"))
              .build();

      userRepository.save(user);

      UserRole userRole = new UserRole();
      userRole.addRole(roleRepository.findById(2L).orElseThrow());
      userRole.addUser(user);

      userRoleRepository.save(userRole);
      return new CustomUserDetail(user, oAuth2User.getAttributes(), userService);
    }

    return new CustomUserDetail(findUser.get(), oAuth2User.getAttributes(), userService);
  }
}
