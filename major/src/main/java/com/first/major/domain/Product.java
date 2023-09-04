package com.first.major.domain;

import com.first.major.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.first.major.controller.AdminController.uploadDir;
import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  private Long id;
  private String name;
  private double price;
  private double weight;
  private String description;
  private String imageName;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  public void addCategory(Category category) {
    this.category = category;
  }

  @Builder
  public Product(String name, double price, double weight, String description, String imageName, Category category) {
    this.name = name;
    this.price = price;
    this.weight = weight;
    this.description = description;
    this.imageName = imageName;
    this.category = category;
  }

  public void changeProduct(ProductDTO productDTO, Category category, MultipartFile file, String imageName) throws IOException {
    String imageUUID;
    if (!file.isEmpty()) {
      imageUUID = file.getOriginalFilename();
      Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
      Files.write(fileNameAndPath, file.getBytes());
    } else {
      imageUUID = null;
    }
    this.name = productDTO.getName();
    this.price = productDTO.getPrice();
    this.weight = productDTO.getWeight();
    this.description = productDTO.getDescription();
    this.imageName = imageUUID;
    this.category = category;
  }
}
