package com.quokka.application.controller;

import com.quokka.application.service.SubCategoryService;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/subcategory"})
public class SubCategoryController {
  @Autowired
  private SubCategoryService subCategorySerive;
  
  @GetMapping({"/list"})
  public List<JSONObject> findAll() {
    return this.subCategorySerive.subCategoryList();
  }
}
