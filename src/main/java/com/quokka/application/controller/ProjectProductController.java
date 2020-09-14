package com.quokka.application.controller;

import com.quokka.application.entity.ProjectProduct;
import com.quokka.application.service.ProductService;
import com.quokka.application.service.ProjectProductService;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/projectProduct"})
public class ProjectProductController {
  @Autowired
  private ProjectProductService prjctPrdctSvc;
  
  @Autowired
  private ProductService productSvc;
  
  @PostMapping({"/add"})
  public ProjectProduct saveProjectProduct(@RequestBody ProjectProduct projectProduct) {
    projectProduct.setProjectProductId(0);
    return this.prjctPrdctSvc.saveProjectProduct(projectProduct);
  }
  
  @GetMapping({"/list/{projectId}"})
  public ResponseEntity<Object> getAllProductsByProjectIdUI(@PathVariable int projectId) {
    List<JSONObject[]> entities = (List)new ArrayList<>();
    entities = this.productSvc.getAllProductsByProjectIdUI(projectId);
    return new ResponseEntity<Object>(entities, HttpStatus.OK);
  }
  
  @GetMapping({"/listHL/{projectId}"})
  public ResponseEntity<Object> getAllProductsByProjectIdHL(@PathVariable int projectId) {
    List<JSONObject[]> entities = (List)new ArrayList<>();
    entities = this.prjctPrdctSvc.getAllProductsByProjectIdHL(projectId);
    return new ResponseEntity<Object>(entities, HttpStatus.OK);
  }
}
