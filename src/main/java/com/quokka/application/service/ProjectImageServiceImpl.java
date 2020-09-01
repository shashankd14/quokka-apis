package com.quokka.application.service;

import com.quokka.application.dao.ProjectImageRepository;
import com.quokka.application.service.ProjectImageService;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectImageServiceImpl implements ProjectImageService {
  @Autowired
  private ProjectImageRepository projectImageRepo;
  
  public List<JSONObject> findImagesByProjectId(Integer projectId) {
    return this.projectImageRepo.findImagesByProjectId(projectId);
  }
  
  public void deleteImagesByProjectId(Integer projectId) {
    this.projectImageRepo.deleteByProjectId(projectId);
  }
  
  public void deleteProjectImageById(Integer imageId) {
    this.projectImageRepo.deleteProjectImageById(imageId);
  }
}
