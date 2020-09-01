package com.quokka.application.service;

import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;

public interface ProductImageService {
  List<JSONObject> findImagesByProductId(@Param("productId") Integer productId);
  
  public void deleteProductImageById(@Param("imageId") Integer imageId);
}
