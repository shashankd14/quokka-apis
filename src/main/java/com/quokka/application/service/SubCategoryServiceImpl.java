package com.quokka.application.service;

import com.quokka.application.dao.SubCategoryRepository;
import com.quokka.application.service.SubCategoryService;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
  @Autowired
  private SubCategoryRepository subCategoryRepo;
  
  public List<JSONObject> subCategoryList() {
    return this.subCategoryRepo.subcategoryList();
  }
}
