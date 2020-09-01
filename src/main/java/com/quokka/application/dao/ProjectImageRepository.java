package com.quokka.application.dao;

import com.quokka.application.entity.ProjectImage;
import java.util.List;
import javax.transaction.Transactional;
import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Integer> {
  @Query(nativeQuery = true, value = "SELECT imageId, imageUrl from project_images where projectId = :projectId ")
  List<JSONObject> findImagesByProjectId(@Param("projectId") Integer projectId);
  
  @Modifying
  @Transactional
  @Query(nativeQuery = true, value = "delete from project_images where projectId = :projectId ")
  void deleteByProjectId(@Param("projectId") Integer projectId);
  
  @Modifying
  @Transactional
  @Query(nativeQuery = true, value = "delete from project_images where imageId = :imageId ")
  void deleteProjectImageById(@Param("imageId") Integer imageId);
}
