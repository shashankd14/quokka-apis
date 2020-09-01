package com.quokka.application.service;

import com.quokka.application.entity.ProjectProduct;
import java.util.List;
import net.minidev.json.JSONObject;

public interface ProjectProductService {
  ProjectProduct saveProjectProduct(ProjectProduct paramProjectProduct);
  
  List<JSONObject[]> getAllProductsByProjectIdHL(int paramInt);
}
