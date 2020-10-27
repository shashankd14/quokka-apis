package com.quokka.application.service;

import com.quokka.application.entity.Project;
import java.util.List;
import net.minidev.json.JSONObject;

public interface ProjectService {
  Project saveProject(Project paramProject);
  
  Project findById(int paramInt);
  
  List<JSONObject> getAllProjects(int userId);
  
  void deleteProject(int paramInt);
  
  List<Project> findProjectDetailsByUserId(int paramInt);
  
  void updateProjectStatus(int paramInt);
  
  public List<Project> getAdminProjectList();
}
