package com.quokka.application.service;

import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;

public interface ProjectImageService {
  List<JSONObject> findImagesByProjectId(@Param("projectId") Integer projectId);
  
  void deleteImagesByProjectId(@Param("projectId") Integer paramInteger);
  
  void deleteProjectImageById(@Param("imageId") Integer paramInteger);
}
