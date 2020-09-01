package com.quokka.application.controller;

import com.quokka.application.service.ProductCategoryService;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/productCategory"})
public class ProductCategoryController {
  @Autowired
  private ProductCategoryService productCatService;
  
  @GetMapping({"/list"})
  public List<JSONObject> findAll() {
    return this.productCatService.findAll();
  }
}
