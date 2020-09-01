package com.quokka.application.dao;

import com.quokka.application.entity.Subcategory;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubCategoryRepository extends JpaRepository<Subcategory, Integer> {
  @Query(nativeQuery = true, value = "SELECT *  FROM subcategories ")
  List<JSONObject> subcategoryList();
}
