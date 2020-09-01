package com.quokka.application.controller;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.StandardBlobTier;
import com.quokka.application.dao.FloorPlanRepository;
import com.quokka.application.dao.ProjectImageRepository;
import com.quokka.application.dao.ProjectRepository;
import com.quokka.application.entity.Project;
import com.quokka.application.entity.ProjectImage;
import com.quokka.application.service.ProjectImageService;
import com.quokka.application.service.ProjectService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/api/project"})
public class ProjectController {
  @Autowired
  private ProjectRepository projectRepo;
  
  @Autowired
  private FloorPlanRepository floorPlanRepo;
  
  @Autowired
  private CloudStorageAccount cloudStorageAccount;
  
  @Autowired
  private ProjectService projectService;
  
  @Autowired
  private ProjectImageRepository projectImageRepo;
  
  @Autowired
  private ProjectImageService projectImgSvc;
  
  @PostMapping({"/add"})
	public ResponseEntity<Object> saveProjectDetails(@RequestParam("name") String name,
			@RequestParam("noOfFloors") int noOfFloors,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "floorPlan", required = false) MultipartFile floorPlan,
			@RequestParam(value = "image", required = false) List<MultipartFile> images) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			Project project = new Project();
			project.setProjectId(0);
			project.setName(name);
			project.setNoOfFloors(noOfFloors);
			project.setUploadStatus(3);
			project.setDescription(description);
			project.setCreatedBy(1);
			project.setUpdatedBy(1);
			project.setCreatedOn(timestamp);
			project.setUpdatedOn(timestamp);
			project.setIsDeleted(Boolean.valueOf(false));
			if (floorPlan != null) {
				String url = uplodaToAzureStorage(floorPlan);
				project.setFloorPlan(url);
			}
			Project p = (Project) this.projectRepo.save(project);
			System.out.println(p.getProjectId());
			for (MultipartFile image : images) {
				ProjectImage projectImage = new ProjectImage();
				String projectImageurl = uplodaToAzureStorage(image);
				projectImage.setImageId(0);
				projectImage.setImageUrl(projectImageurl);
				projectImage.setProjectId(p.getProjectId());
				projectImage.setCreatedBy(1);
				projectImage.setUpdatedBy(1);
				projectImage.setCreatedOn(timestamp);
				projectImage.setUpdatedOn(timestamp);
				projectImage.setDeleted(false);
				this.projectImageRepo.save(projectImage);
			}
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity("Project saved", HttpStatus.OK);
	}
  
  @PutMapping({"/update"})
	public ResponseEntity<Object> updateProjectDetails(@RequestParam("projectId") int projectId,
			@RequestParam("name") String name, @RequestParam("noOfFloors") int noOfFloors,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "floorPlan", required = false) MultipartFile floorPlan,
			@RequestParam(value = "image", required = false) List<MultipartFile> images,
			@RequestParam(value = "imageId", required = false) int[] imageIds) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Project project = new Project();
		project.setProjectId(projectId);
		project.setName(name);
		project.setNoOfFloors(noOfFloors);
		project.setUploadStatus(5);
		project.setDescription(description);
		project.setCreatedBy(1);
		project.setUpdatedBy(1);
		project.setCreatedOn(timestamp);
		project.setUpdatedOn(timestamp);
		project.setIsDeleted(Boolean.valueOf(false));
		if (floorPlan != null) {
			String url = uplodaToAzureStorage(floorPlan);
			project.setFloorPlan(url);
		}
		Project p = (Project) this.projectRepo.save(project);
		for (MultipartFile image : images) {
			ProjectImage projectImage = new ProjectImage();
			String projectImageurl = uplodaToAzureStorage(image);
			projectImage.setImageId(0);
			projectImage.setImageUrl(projectImageurl);
			projectImage.setProjectId(p.getProjectId());
			projectImage.setCreatedBy(1);
			projectImage.setUpdatedBy(1);
			projectImage.setCreatedOn(timestamp);
			projectImage.setUpdatedOn(timestamp);
			projectImage.setDeleted(false);
			this.projectImageRepo.save(projectImage);
		}
		if (imageIds != null)
			for (int id : imageIds)
				this.projectImgSvc.deleteProjectImageById(Integer.valueOf(id));
		return new ResponseEntity("Project updated successfully", HttpStatus.OK);
	}
  
  @PutMapping({"/status-InProgress/{projectId}"})
  public ResponseEntity<Object> updateProjectStatus(@PathVariable int projectId) {
    try {
      Project theProject = this.projectService.findById(projectId);
      theProject.setUploadStatus(4);
      this.projectService.saveProject(theProject);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
    return new ResponseEntity("Project updated successfully", HttpStatus.OK);
  }
  
  @PutMapping({"/addProjectAssetBundle"})
  public String addProjectAssetBundle(@RequestParam("projectId") int projectId, @RequestParam("projectAssetBundle") MultipartFile assetBundle) {
    String assetBundleURL = uplodaToAzureStorage(assetBundle);
    Optional<Project> result = this.projectRepo.findById(Integer.valueOf(projectId));
    Project theProject = null;
    if (result.isPresent()) {
      theProject = result.get();
    } else {
      throw new RuntimeException("Did not find Project Id - " + projectId);
    } 
    theProject.setAssetBundleUrl(assetBundleURL);
    theProject.setUploadStatus(5);
    this.projectRepo.save(theProject);
    return "Asset bundle for project ID: " + projectId + " uploaded successful!!";
  }
  
  @DeleteMapping({"/delete/{projectId}"})
  public ResponseEntity<Object> deleteProject(@PathVariable int projectId) {
    this.projectService.deleteProject(projectId);
    return new ResponseEntity("Project deleted successfully", HttpStatus.OK);
  }
  
  @GetMapping({"/listByUserId"})
  public List<JSONObject[]> getProjectDetails(@RequestParam("userId") int userId) {
    return this.projectService.findProjectDetailsByUserId(userId);
  }
  
  @GetMapping({"/getById/{projectId}"})
  public ResponseEntity<Object> findById(@PathVariable int projectId) {
    try {
      Project theProject = this.projectService.findById(projectId);
      return new ResponseEntity(theProject, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
  }
  
  @GetMapping({"/list"})
  public ResponseEntity<Object> getAllProjects() {
    try {
      List<JSONObject> entities = new ArrayList<>();
      entities = this.projectService.getAllProjects();
      return new ResponseEntity(entities, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
  }
  
  @GetMapping({"/getImagesById/{projectId}"})
  public ResponseEntity<Object> findImagesByProjectId(@PathVariable("projectId") Integer projectId) {
    try {
      List<JSONObject> entities = new ArrayList<>();
      entities = this.projectImgSvc.findImagesByProjectId(projectId);
      return new ResponseEntity(entities, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
  }
  
  @DeleteMapping({"/deleteImagesByProjectId/{projectId}"})
  public ResponseEntity<Object> deleteByProjectId(@Param("projectId") Integer projectId) {
    try {
      this.projectImgSvc.deleteImagesByProjectId(projectId);
      return new ResponseEntity("Delete successful", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
  }
  
  public String uplodaToAzureStorage(MultipartFile file) {
    CloudBlockBlob blob = null;
    try {
      CloudBlobClient blobClient = this.cloudStorageAccount.createCloudBlobClient();
      CloudBlobContainer container = blobClient.getContainerReference("project");
      blob = container.getBlockBlobReference(file.getOriginalFilename());
      blob.upload(file.getInputStream(), -1L);
      blob.uploadStandardBlobTier(StandardBlobTier.COOL);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (StorageException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return blob.getUri().toString();
  }
}
