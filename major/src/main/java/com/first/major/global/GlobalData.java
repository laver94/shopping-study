package com.first.major.global;

import com.first.major.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {
  public static List<Product> cart;
  static {
    cart = new ArrayList<>();
  }
}
