package com.quokka.application.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.storage.StorageScopes;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.quokka.application.dao.FloorPlanRepository;
import com.quokka.application.dao.ProjectImageRepository;
import com.quokka.application.dao.ProjectRepository;
import com.quokka.application.entity.Product;
import com.quokka.application.entity.Project;
import com.quokka.application.entity.ProjectImage;
import com.quokka.application.service.ProjectImageService;
import com.quokka.application.service.ProjectService;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping({"/api/project"})
public class ProjectController {
  @Autowired
  private ProjectRepository projectRepo;
  
  @Autowired
  private FloorPlanRepository floorPlanRepo;
  
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
			@RequestParam(value = "image", required = false) List<MultipartFile> images,
			@RequestParam("userId") int userId) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			Project project = new Project();
			project.setProjectId(0);
			project.setName(name);
			project.setNoOfFloors(noOfFloors);
			project.setUploadStatus(3);
			project.setDescription(description);
			project.setCreatedBy(userId);
			project.setUpdatedBy(userId);
			project.setCreatedOn(timestamp);
			project.setUpdatedOn(timestamp);
			project.setIsDeleted(Boolean.valueOf(false));
			if (floorPlan != null) {
				String url = uploadToBucket(floorPlan);
				project.setFloorPlan(url);
			}
			Project p = (Project) this.projectRepo.save(project);
			for (MultipartFile image : images) {
				ProjectImage projectImage = new ProjectImage();
				String projectImageurl = uploadToBucket(image);
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
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>("Project saved", HttpStatus.OK);
	}
  
  @PutMapping({"/update"})
	public ResponseEntity<Object> updateProjectDetails(@RequestParam("projectId") int projectId,
			@RequestParam("name") String name, @RequestParam("noOfFloors") int noOfFloors,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "floorPlan", required = false) MultipartFile floorPlan,
			@RequestParam(value = "image", required = false) List<MultipartFile> images,
			@RequestParam(value = "imageId", required = false) int[] imageIds,
			@RequestParam("userId") int userId) {
	  
	  Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	  try {
		  
		  Project project = projectService.findById(projectId);
			// project.setProjectId(projectId);
			project.setName(name);
			project.setNoOfFloors(noOfFloors);
			project.setUploadStatus(5);
			project.setDescription(description);
			project.setCreatedBy(userId);
			project.setUpdatedBy(userId);
			project.setCreatedOn(timestamp);
			project.setUpdatedOn(timestamp);
			project.setIsDeleted(Boolean.valueOf(false));
			if (floorPlan != null) {
				String url = uploadToBucket(floorPlan);
				project.setFloorPlan(url);
			}
			Project p = (Project) this.projectRepo.save(project);
			for (MultipartFile image : images) {
				ProjectImage projectImage = new ProjectImage();
				String projectImageurl = uploadToBucket(image);
				projectImage.setImageId(0);
				projectImage.setImageUrl(projectImageurl);
				projectImage.setProjectId(p.getProjectId());
				projectImage.setCreatedBy(userId);
				projectImage.setUpdatedBy(userId);
				projectImage.setCreatedOn(timestamp);
				projectImage.setUpdatedOn(timestamp);
				projectImage.setDeleted(false);
				this.projectImageRepo.save(projectImage);
			}
			if (imageIds != null)
				for (int id : imageIds)
					this.projectImgSvc.deleteProjectImageById(Integer.valueOf(id));
			return new ResponseEntity<Object>("Project updated successfully", HttpStatus.OK);
	  }catch(Exception e) {
		  
		  return new ResponseEntity<Object>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	  }
		
		
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
  
  @PutMapping({"/addAssetBundle"})
	public String addProjectAssetBundle(@RequestParam("projectId") int projectId,
			@RequestParam("projectAssetBundle") MultipartFile assetBundle) {
    String assetBundleURL = resumableUpload(assetBundle);
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
	public ResponseEntity<Object> getProjectDetails(@RequestParam("userId") int userId) {

		int[] adminArray = { 83, 84, 85 };

		try {
			
			List<Project> projectList = new ArrayList<Project>();
			if (Arrays.stream(adminArray).anyMatch(i -> i == userId)) {

				projectList = projectService.getAdminProjectList();
			} else {
				
				projectList = projectService.findProjectDetailsByUserId(userId);
			}
			return new ResponseEntity<Object>(projectList, HttpStatus.OK);
		}
		catch(Exception e) {
			
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.OK);
		}
		
		// return this.projectService.findProjectDetailsByUserId(userId);
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
  public ResponseEntity<Object> getAllProjects(int userId) {
    try {
      List<JSONObject> entities = new ArrayList<>();
      entities = this.projectService.getAllProjects(userId);
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
  
  public String uploadToBucket(@RequestParam("file") MultipartFile file) {

		String uploadUrl = "";
		try {
			String projectId = "quokka-project-alpha";
			String bucketName = "quokka-project-uploads";
			String fileName = file.getOriginalFilename();

			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

			BlobId blobId = BlobId.of(bucketName, fileName);

			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

			byte[] bytes = file.getBytes();

			Blob blob = storage.create(blobInfo, bytes);

			uploadUrl = "https://storage.googleapis.com/quokka-project-uploads/"+fileName;
			

		} catch (IOException e) {
			e.printStackTrace();
		}

		return uploadUrl;
	}
  
	public String resumableUpload(MultipartFile file) {

		String uploadUrl = "https://storage.googleapis.com/quokka-project-uploads/" + file.getOriginalFilename();
		try {

			
			System.out.println("file : "+file.getOriginalFilename());
			Credentials credentials = GoogleCredentials
					.fromStream(new FileInputStream("src/main/resources/quokka-project-alpha-key.json"));
			InputStreamContent mediaContent = new InputStreamContent("application/octet-stream", file.getInputStream());
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			HttpRequestInitializer httpRequestInitializer = null;
			GenericUrl genericUrl = new GenericUrl("https://www.googleapis.com/upload/storage/v1/b/"
					+ "quokka-project-uploads" + "/o?uploadType=resumable&name=" + file.getOriginalFilename());
			
			System.out.println("generic Url "+genericUrl );
			MediaHttpUploader uploader = new MediaHttpUploader(mediaContent, httpTransport, httpRequestInitializer);
			HttpHeaders requestHeaders = new HttpHeaders();
			credentials = ((GoogleCredentials) credentials).createScoped(StorageScopes.all());
			Map<String, List<String>> credentialHeaders = credentials.getRequestMetadata();
			if (credentialHeaders == null) {
				return "";
			}
			for (Map.Entry<String, List<String>> entry : credentialHeaders.entrySet()) {
				String headerName = entry.getKey();
				List<String> requestValues = new ArrayList<>();
				requestValues.addAll(entry.getValue());
				requestHeaders.put(headerName, requestValues);
			}

			HttpResponse response = uploader.setInitiationHeaders(requestHeaders).setDisableGZipContent(true)
					.upload(genericUrl);

		} catch (Exception e) {
			return e.getMessage();
		}
		return uploadUrl;

	}
  
  
}
