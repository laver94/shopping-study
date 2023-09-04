package com.first.major.controller;

import com.first.major.domain.Product;
import com.first.major.global.GlobalData;
import com.first.major.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.first.major.global.GlobalData.*;

@Controller
@RequiredArgsConstructor
public class CartController {

  private final ProductService productService;

  @GetMapping("/addToCart/{id}")
  public String addToCart(@PathVariable Long id) {
    cart.add(productService.getProductById(id).orElseThrow());
    return "redirect:/shop";
  }

  @GetMapping("/cart")
  public String cartGet(Model model) {
    model.addAttribute("cartCount", cart.size());
    model.addAttribute("total", cart.stream().mapToDouble(Product::getPrice).sum());
    model.addAttribute("cart", cart);
    return "cart";
  }


}
