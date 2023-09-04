package com.first.major.service;

import com.first.major.dto.ProductDTO;
import com.first.major.domain.Category;
import com.first.major.domain.Product;
import com.first.major.repository.CategoryRepository;
import com.first.major.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public List<Product> getAllProduct() {
    return productRepository.findAll();
  }

  public void addProduct(Product product) {
    productRepository.save(product);
  }

  public void removeProductById(Long id) {
    productRepository.deleteById(id);
  }

  public Optional<Product> getProductById(Long id) {
    return productRepository.findById(id);
  }

  public List<Product> getAllProductsByCategoryId(Long id) {
    Optional<Category> category = categoryRepository.findById(id);
    return productRepository.findAllByCategory(category.orElse(null));
  }

  public void changeProduct(ProductDTO productDTO, Category category, MultipartFile file, String imageName) throws IOException {
    Product product = getProductById(productDTO.getId()).orElseThrow();
    product.changeProduct(productDTO,
            categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(), file, imageName);
  }

}
