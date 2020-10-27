package com.quokka.application.dao;

import com.quokka.application.entity.Product;
import com.quokka.application.entity.Project;
import java.util.List;
import javax.transaction.Transactional;
import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
	@Query(nativeQuery = true, value = "select * from projects where createdBy= :userId")
	List<Project> getProjectDetailsByUserId(@Param("userId") int userId);
	
	@Query(nativeQuery = true, value = "select * from projects")
	List<Project> getAdminProjectList();

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update projects set uploadstatus = 4 where projectId = :projectId ")
	void updateProjectStatus(@Param("projectId") int projectId);

	@Query(nativeQuery = true, value = "SELECT  projects.*, COUNT(images.imageId) AS imageCount  FROM projects "
			+ "LEFT JOIN  project_images images  ON images.projectId = projects.projectId  "
			+ " where projects.createdBy = :userId " + " GROUP BY  projects.projectId")
	public List<JSONObject> getAllProjects(@Param("userId") int userId);

}
