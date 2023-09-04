package com.first.major.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @NotEmpty
  @Column(nullable = false)
  private String firstName;

  private String lastName;

  @Column(nullable = false, unique = true)
  @NotEmpty
  @Email(message = "{errors.invalid_email}")
  private String email;

  @NotEmpty
  private String password;

//  @OneToMany(mappedBy = "user")
//  private List<UserRole> userRoles = new ArrayList<>();

  @Builder
  public User(String firstName, String lastName, String email, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }

  public User(User user) {
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.password = user.getPassword();
  }

  public void encode(String password) {
    this.password = password;
  }

}
