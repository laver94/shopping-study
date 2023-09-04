package com.first.major.service;

import com.first.major.domain.Category;
import com.first.major.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public void addCategory(Category category) {
    categoryRepository.save(category);
  }

  public List<Category> getAllCategory() {
    return categoryRepository.findAll();
  }

  public void removeCategoryById(Long id) {
    categoryRepository.deleteById(id);
  }

  public Optional<Category> getCategoryById(Long id) {
    return categoryRepository.findById(id);
  }

}
