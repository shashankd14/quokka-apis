package com.quokka.application.service;

import com.quokka.application.entity.Project;
import java.util.List;
import net.minidev.json.JSONObject;

public interface ProjectService {
  Project saveProject(Project paramProject);
  
  Project findById(int paramInt);
  
  List<JSONObject> getAllProjects();
  
  void deleteProject(int paramInt);
  
  List<JSONObject[]> findProjectDetailsByUserId(int paramInt);
  
  void updateProjectStatus(int paramInt);
}
