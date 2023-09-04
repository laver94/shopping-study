package com.first.major.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  @Email @NotEmpty
  private String email;
  @NotEmpty
  private String password;
  @NotEmpty
  private String firstName;
  @NotEmpty
  private String lastName;

}
