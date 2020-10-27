package com.quokka.application.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.google.api.client.util.IOUtils;
import com.google.api.services.storage.StorageScopes;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RestController
@RequestMapping("/gcs-upload")
public class ResumableUploadController {

	@PostMapping("/createFile")
	public String upload(@RequestParam("fileName")String fileName) {
		
	/*	try {
			
			//System.out.println("length: "+requestEntity.getContentLength());
			Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("E:\\quokka\\keys\\quokka-project-alpha-823d051c43b3.json"));
			
			
			System.out.println("credentials: "+credentials.getAuthenticationType());
			//System.out.println("size: "+stream.b);
			
		    InputStreamContent mediaContent = new InputStreamContent("application/octet-stream", stream);
		    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		    HttpRequestInitializer httpRequestInitializer = null;
		    GenericUrl genericUrl = new GenericUrl(
		            "https://www.googleapis.com/upload/storage/v1/b/" + "quokka-project-uploads" + "/o?uploadType=resumable&name=" + 
		    		"21test4789.dollhose");
		    
		    MediaHttpUploader uploader = new MediaHttpUploader(mediaContent, httpTransport, httpRequestInitializer);
		    HttpHeaders requestHeaders = new HttpHeaders();
		    
		    requestHeaders.setContentLength((long) 100);
		    requestHeaders.setContentType("application/offset+octet-stream");
		    credentials = ((GoogleCredentials) credentials).createScoped(StorageScopes.all());
		    
		    System.out.println("mediaContent: "+mediaContent.getType());
		    
		    System.out.println("mediaContent.getLength: "+mediaContent.getLength()); ;
		    System.out.println("request metadata: "+credentials.getRequestMetadata());
		   
		    Map<String, List<String>> credentialHeaders = credentials.getRequestMetadata();
		    if (credentialHeaders == null) {
		        return "";
		    }
		    for (Map.Entry<String, List<String>> entry : credentialHeaders.entrySet()) {
		        String headerName = entry.getKey();
		        List<String> requestValues = new ArrayList<>();
		        requestValues.addAll(entry.getValue());
		        requestHeaders.put(headerName, requestValues);
		        System.out.println("headerName: "+headerName);
		        System.out.println("requestValues: "+requestValues);
		    }

		    System.out.println("credentialHeaders: "+credentialHeaders.toString());

		HttpResponse response = uploader.setInitiationHeaders(requestHeaders).setDisableGZipContent(true)
		                .upload(genericUrl);
		
		System.out.println("statuscode: "+response.getStatusCode());
		System.out.println("parseAsString: "+response.parseAsString());
			
		}catch(Exception e) {
			return ("error: "+e.getMessage());
		}
		*/
		
		OkHttpClient client = new OkHttpClient();
	//	AppIdentityService appIdentityService = credential.getAppIdentityService();
	//	Collection<String> scopes = credential.getScopes();
	//	String accessToken = appIdentityService.getAccessToken(scopes).getAccessToken();
		
		String response1 = "";
		
		try {
			

			Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("E:\\quokka\\keys\\quokka-project-alpha-823d051c43b3.json"));
			credentials = ((GoogleCredentials) credentials).createScoped(StorageScopes.all());
			
			   System.out.println("Authorization: "+credentials.getRequestMetadata().get("Authorization").get(0));
			   System.out.println("request metadata: "+credentials.getRequestMetadata());
			Request request = new Request.Builder()
			        .url("https://storage.googleapis.com/upload/storage/v1/b/" + "quokka-project-uploads" + "/o?name=" + fileName + "&uploadType=resumable")
			        .post(RequestBody.create(MediaType.parse("application/octet-stream"), new byte[0]))
			        .addHeader("X-Upload-Content-Type", "application/offset+octet-stream")
			        .addHeader("X-Upload-Content-Length", "" + 70000000)
			     //   .addHeader("Origin", "http://localhost:8080")
			        .addHeader("Origin", "*")
			        .addHeader("Authorization", credentials.getRequestMetadata().get("Authorization").get(0))
			        .build();
			Response response = client.newCall(request).execute();
			response1 = response.header("location");
			
			response.body().close();
			
			System.out.println("request: "+request);
			return response.header("location");
			
			
		}catch(Exception e) {
			
		}
		
		
		
		return response1;
	}
	
	@PutMapping("/fileUpload")
	public ResponseEntity<Object> uploadFile(@RequestParam ("file")MultipartFile file, 
			@RequestParam("url") String url){
		
		try {
			
			OkHttpClient client = new OkHttpClient();
			
			File convFile = new File(file.getOriginalFilename());
			  convFile.createNewFile(); 
			  FileOutputStream fos = new FileOutputStream(convFile); 
			  fos.write(file.getBytes());
			  fos.close(); 
			 

			MediaType MEDIA_TYPE_MARKDOWN
			        = MediaType.parse("text/x-markdown; charset=utf-8");

		//	file.
		//	byte[] targetArray = IOUtils.toByteArray(stream);
		//	InputStream inputStream = getAssets().open("README.md");

			RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), convFile);
			
			 System.out.println("requestBody: "+requestBody);
			Request request = new Request.Builder()
			        .url(url)
			        .put(requestBody)
			        .build();

			Response response = client.newCall(request).execute();
			
			System.out.println("response: "+response);
			
			response.body().close();
			return new ResponseEntity<Object>(response, HttpStatus.OK); 
			
		}catch(Exception e){
			
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}
	
}
