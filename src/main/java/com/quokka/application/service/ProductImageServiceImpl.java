package com.quokka.application.service;

import com.quokka.application.dao.ProductImageRepository;
import com.quokka.application.service.ProductImageService;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageServiceImpl implements ProductImageService {
  @Autowired
  private ProductImageRepository productImgrepo;
  
  public List<JSONObject> findImagesByProductId(Integer productId) {
    return this.productImgrepo.findImagesByProductId(productId);
  }
  
  public void deleteProductImageById(Integer imageId) {
    this.productImgrepo.deleteProductImageById(imageId);
  }
}
