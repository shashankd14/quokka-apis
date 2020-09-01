package com.quokka.application.service;

import com.quokka.application.dao.ProjectProductRepository;
import com.quokka.application.entity.ProjectProduct;
import com.quokka.application.service.ProjectProductService;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectProductServiceImpl implements ProjectProductService {
  private ProjectProductRepository prjPrdctRepo;
  
  @Autowired
  public ProjectProductServiceImpl(ProjectProductRepository thePrjPrdctRepo) {
    this.prjPrdctRepo = thePrjPrdctRepo;
  }
  
  public ProjectProduct saveProjectProduct(ProjectProduct projectProduct) {
    return (ProjectProduct)this.prjPrdctRepo.save(projectProduct);
  }
  
  public List<JSONObject[]> getAllProductsByProjectIdHL(int projectId) {
    return this.prjPrdctRepo.getAllProductsByProjectIdHL(projectId);
  }
}
