package com.first.major.controller;

import com.first.major.dto.ProductDTO;
import com.first.major.global.GlobalData;
import com.first.major.service.CategoryService;
import com.first.major.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.first.major.global.GlobalData.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

  private final CategoryService categoryService;
  private final ProductService productService;

  @GetMapping({"/", "home"})
  public String home(Model model) {
    model.addAttribute("cartCount", cart.size());
    return "index";
  }
  @GetMapping("/shop")
  public String shop(Model model) {
    model.addAttribute("categories", categoryService.getAllCategory());
    model.addAttribute("products", productService.getAllProduct());
    return "shop";
  }

  @GetMapping("/shop/category/{id}")
  public String shopByCategory(@PathVariable Long id, Model model) {
    model.addAttribute("categories", categoryService.getAllCategory());
    model.addAttribute("products", productService.getAllProductsByCategoryId(id));
    model.addAttribute("cartCount", cart.size());
    return "shop";
  }

  @GetMapping("/shop/viewproduct/{id}")
  public String viewProduct(@PathVariable Long id, Model model) {
    ProductDTO dto = AdminController.toDto(productService.getProductById(id).orElseThrow());
    model.addAttribute("product", dto);
    model.addAttribute("cartCount", cart.size());

    return "viewProduct";
  }






}
