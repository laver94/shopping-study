package com.first.major.dto;

import lombok.Data;

@Data
public class CategoryDTO {

  private Long id;
  private String name;

  public CategoryDTO() {
  }

  public CategoryDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }



}
