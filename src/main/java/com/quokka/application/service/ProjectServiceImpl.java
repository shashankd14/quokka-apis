package com.quokka.application.service;

import com.quokka.application.dao.ProjectRepository;
import com.quokka.application.entity.Project;
import com.quokka.application.service.ProjectService;
import java.util.List;
import java.util.Optional;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {
  private ProjectRepository projectRepo;
  
  @Autowired
  public ProjectServiceImpl(ProjectRepository theProjectRepo) {
    this.projectRepo = theProjectRepo;
  }
  
  public Project saveProject(Project project) {
    return (Project)this.projectRepo.save(project);
  }
  
  public Project findById(int projectId) {
    Optional<Project> result = this.projectRepo.findById(Integer.valueOf(projectId));
    Project project = null;
    if (result.isPresent()) {
      project = result.get();
    } else {
      throw new RuntimeException("Did not find project id " + projectId);
    } 
    return project;
  }
  
  public List<JSONObject> getAllProjects() {
    return this.projectRepo.getAllProjects();
  }
  
  public void deleteProject(int projectId) {
    this.projectRepo.deleteById(Integer.valueOf(projectId));
  }
  
  public List<JSONObject[]> findProjectDetailsByUserId(int userId) {
    return this.projectRepo.getProjectDetailsByUserId(userId);
  }
  
  public void updateProjectStatus(int projectId) {
    this.projectRepo.updateProjectStatus(projectId);
  }
}
