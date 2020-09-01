package com.quokka.application.dao;

import com.quokka.application.entity.ProductImage;
import java.util.List;
import javax.transaction.Transactional;
import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
  @Query(nativeQuery = true, value = "SELECT * from product_images where productId = :productId ")
  List<JSONObject> findImagesByProductId(@Param("productId") Integer productId);
  
  @Modifying
  @Transactional
  @Query(nativeQuery = true, value = "delete from product_images where imageId = :imageId ")
  public void deleteProductImageById(@Param("imageId") Integer imageId);
}
