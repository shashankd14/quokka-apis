package com.quokka.application.service;

import java.util.List;
import net.minidev.json.JSONObject;

public interface ProductCategoryService {
  List<JSONObject> findAll();
}
