package com.quokka.application.dao;

import com.quokka.application.entity.ProductCategory;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
  @Query(nativeQuery = true, value = "SELECT *  FROM product_categories ")
  List<JSONObject> categoryList();
}
