package com.quokka.application.service;

import com.quokka.application.dao.ProductCategoryRepository;
import com.quokka.application.service.ProductCategoryService;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
  @Autowired
  private ProductCategoryRepository productCatRepo;
  
  public List<JSONObject> findAll() {
    return this.productCatRepo.categoryList();
  }
}
