package com.first.major.controller;

import com.first.major.domain.User;
import com.first.major.domain.UserRole;
import com.first.major.dto.UserDTO;
import com.first.major.repository.RoleRepository;
import com.first.major.repository.UserRepository;
import com.first.major.repository.UserRoleRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserRoleRepository userRoleRepository;

  @GetMapping("/login")
  public String login() {

    return "login";
  }

  @GetMapping("/register")
  public String registerGet(Model model) {
    model.addAttribute("user", new UserDTO());
    return "register";
  }

  @PostMapping("/register")
  public String registerPost(@Validated @ModelAttribute("user") UserDTO userdto, BindingResult bindingResult, HttpServletRequest request) throws ServletException {

    if (userRepository.findUserByEmail(userdto.getEmail()).isPresent()) {
      bindingResult.reject("emailError", "email이 중복입니다.");
    }

    if (bindingResult.hasErrors()) {
      return "register";
    }

    String password = userdto.getPassword();
    User user = User.builder()
            .email(userdto.getEmail())
            .firstName(userdto.getFirstName())
            .lastName(userdto.getLastName())
            .password(bCryptPasswordEncoder.encode(password))
            .build();
    UserRole userRole = new UserRole();
    userRole.addRole(roleRepository.findById(2L).orElseThrow());
    userRole.addUser(user);

    userRepository.save(user);
    userRoleRepository.save(userRole);

    request.login(user.getEmail(), password);
    return "redirect:/";
  }

}
