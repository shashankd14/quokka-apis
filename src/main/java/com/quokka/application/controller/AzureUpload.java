package com.quokka.application.controller;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.StandardBlobTier;
import com.quokka.application.dao.ProductImageRepository;
import com.quokka.application.entity.ProductImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AzureUpload {
  @Autowired
  private CloudStorageAccount cloudStorageAccount;
  
  @Autowired
  private ProductImageRepository productImgRepo;
  
  @PostMapping({"/product-image/upload"})
  public List<URI> upload(@RequestParam("file") List<MultipartFile> multipartFiles, @RequestParam("productId") int pId, @RequestParam("user") int user) {
    List<URI> uri = new ArrayList<>();
    CloudBlockBlob blob = null;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    for (MultipartFile file : multipartFiles) {
      ProductImage img = new ProductImage();
      try {
        CloudBlobClient blobClient = this.cloudStorageAccount.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference("product");
        blob = container.getBlockBlobReference(file.getOriginalFilename());
        blob.upload(file.getInputStream(), -1L);
        blob.uploadStandardBlobTier(StandardBlobTier.COOL);
        uri.add(blob.getUri());
        img.setCreatedBy(user);
        img.setUpdatedBy(user);
        img.setImageUrl(blob.getUri().toString());
        img.setIsDeleted(false);
        //img.setProductId(pId);
        img.setUpdatedOn(timestamp);
        img.setCreatedOn(timestamp);
        this.productImgRepo.save(img);
      } catch (URISyntaxException e) {
        e.printStackTrace();
      } catch (StorageException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } 
    } 
    return uri;
  }
}
