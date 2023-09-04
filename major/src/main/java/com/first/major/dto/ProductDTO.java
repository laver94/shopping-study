package com.first.major.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
  private Long id;
  private String name;
  private Long categoryId;
  private String categoryName;
  private double price;
  private double weight;
  private String description;
  private String imageName;

}
