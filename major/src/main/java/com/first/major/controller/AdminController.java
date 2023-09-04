package com.first.major.controller;

import com.first.major.dto.ProductDTO;
import com.first.major.domain.Category;
import com.first.major.dto.CategoryDTO;
import com.first.major.domain.Product;
import com.first.major.service.CategoryService;
import com.first.major.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

  public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

  private final CategoryService categoryService;
  private final ProductService productService;

  @GetMapping
  public String adminHome() {
    return "adminHome";
  }

  @GetMapping("/categories")
  public String getCat(Model model) {
    model.addAttribute("categories", getCategoryDTOList());
    return "categories";
  }

  @GetMapping("/categories/add")
  public String getCatAdd(Model model) {
    model.addAttribute("category", new CategoryDTO());
    return "categoriesAdd";
  }

  @PostMapping("/categories/add")
  public String postCatAdd(@ModelAttribute("category") CategoryDTO categoryDto) {
    categoryService.addCategory(toEntity(categoryDto));
    return "redirect:/admin/categories";
  }

  @GetMapping("/categories/delete/{id}")
  public String deleteCat(@PathVariable Long id) {
    categoryService.removeCategoryById(id);
    return "redirect:/admin/categories";
  }

  @GetMapping("/categories/update/{id}")
  public String updateCat(@PathVariable Long id, Model model) {
    Optional<Category> category = categoryService.getCategoryById(id);
    if (category.isPresent()) {
      model.addAttribute("category", toDto(category.get()));
      return "categoriesAdd";
    } else {
      return "404";
    }
  }

  private List<CategoryDTO> getCategoryDTOList() {
    return categoryService.getAllCategory().stream()
            .map(AdminController::toDto)
            .collect(toList());
  }

  public static CategoryDTO toDto(Category category) {
    return new CategoryDTO(category.getId(), category.getName());
  }

  public static Category toEntity(CategoryDTO categoryDto) {
    return Category.builder()
            .id(categoryDto.getId())
            .name(categoryDto.getName())
            .build();
  }

  /**
   * Product section
   */
  @GetMapping("/products")
  public String products(Model model) {
    List<ProductDTO> dtoList = productService.getAllProduct().stream()
            .map(AdminController::toDto)
            .collect(toList());
    model.addAttribute("products", dtoList);
    return "products";
  }

  @GetMapping("/products/add")
  public String productsAddGet(Model model) {
    model.addAttribute("productDTO", new ProductDTO());
    model.addAttribute("categories", getCategoryDTOList());
    return "productsAdd";
  }

  @PostMapping("/products/add")
  public String productAddPost(@ModelAttribute ProductDTO productDTO,
                                @RequestParam("productImage")MultipartFile file,
                                @RequestParam("imgName") String imgName) throws IOException {
    //새로 만들기 or 업데이트
    if (productDTO.getId() == null) {
      Product product = toEntity(productDTO,
              categoryService.getCategoryById(productDTO.getCategoryId()).orElseThrow(), file, imgName);
      productService.addProduct(product);
    } else {
      productService.changeProduct(productDTO,
              categoryService.getCategoryById(productDTO.getCategoryId()).orElseThrow(), file, imgName);
    }
    return "redirect:/admin/products";
  }

  @GetMapping("/product/delete/{id}")
  public String deleteProduct(@PathVariable("id") Long id) throws IOException{
    Product product = productService.getProductById(id).orElseThrow();
    if (product.getImageName() != null) {
      Path fileNameAndPath = Paths.get(uploadDir, product.getImageName());
      Files.delete(fileNameAndPath);
    }
    productService.removeProductById(id);
    return "redirect:/admin/products";
  }

  @GetMapping("/product/update/{id}")
  public String updateProductGet(@PathVariable Long id, Model model) {
    Product product = productService.getProductById(id).orElseThrow();
    model.addAttribute("productDTO", toDto(product));
    model.addAttribute("categories", getCategoryDTOList());
    return "productsAdd";
  }

  public static ProductDTO toDto(Product product) {
    return new ProductDTO(product.getId(),
            product.getName(),
            product.getCategory().getId(),
            product.getCategory().getName(),
            product.getPrice(),
            product.getWeight(),
            product.getDescription(),
            product.getImageName()
    );
  }

  public static Product toEntity(ProductDTO productDTO, Category category, MultipartFile file, String imageName) throws IOException {

    String imageUUID;
    if (!file.isEmpty()) {
      imageUUID = file.getOriginalFilename();
      Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
      Files.write(fileNameAndPath, file.getBytes());
    } else {
      imageUUID = null;
    }
    Product product = Product.builder()
            .name(productDTO.getName())
            .price(productDTO.getPrice())
            .weight(productDTO.getWeight())
            .description(productDTO.getDescription())
            .imageName(imageUUID)
            .build();
    product.addCategory(category);
    return product;
  }


}
