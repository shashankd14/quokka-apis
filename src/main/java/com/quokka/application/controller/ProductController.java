package com.quokka.application.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.StandardBlobTier;
import com.quokka.application.dao.ProductCategoryRepository;
import com.quokka.application.dao.ProductImageRepository;
import com.quokka.application.dao.ProductRepository;
import com.quokka.application.dao.RoomTypeRepository;
import com.quokka.application.dao.SubCategoryRepository;
import com.quokka.application.dao.TagRepository;
import com.quokka.application.entity.Product;
import com.quokka.application.entity.ProductCategory;
import com.quokka.application.entity.ProductImage;
import com.quokka.application.entity.RoomType;
import com.quokka.application.entity.Subcategory;
import com.quokka.application.entity.Tag;
import com.quokka.application.service.ProductImageService;
import com.quokka.application.service.ProductService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping({"/api/product"})
public class ProductController {
  @Autowired
  private ProductService productService;
  
  @Autowired
  private ProductRepository productRepository;
  
  @Autowired
  private TagRepository tagRepository;
  
  @Autowired
  private CloudStorageAccount cloudStorageAccount;
  
  @Autowired
  private ProductCategoryRepository productCategoryRepository;
  
  @Autowired
  private SubCategoryRepository subcategoryRepo;
  
  @Autowired
  private ProductImageService productImgSvc;
  
  @Autowired
  private ProductImageRepository productImageRepo;
  
  @Autowired
  private RoomTypeRepository roomTypeRepo;
  
  @PostMapping({"/add"})
	public ResponseEntity<Object> addProduct(@RequestParam("categoryIds") String[] categoryIds,
			@RequestParam(value = "subcategoryIds", required = false) String[] subcategoryIds,
			@RequestParam("name") String name, 
			@RequestParam("displayType") String displayType,
			@RequestParam("description") String description, 
			@RequestParam("brand") String brand,
			@RequestParam("roomType") int roomType, 
			@RequestParam(value = "fabric", required = false) String fabric,
			@RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "primaryMaterial", required = false) String primaryMaterial,
			@RequestParam(value = "purchaseNote", required = false) String purchaseNote,
			@RequestParam("height") float height, 
			@RequestParam("plength") float pLength,
			@RequestParam("width") float width, 
			@RequestParam("weight") float weight,
			@RequestParam("materialInfo") String materialInfo, 
			@RequestParam("price") float price,
			@RequestParam("stock") int stock, 
			@RequestParam("thumbnailImage") MultipartFile thumbnailImage,
			@RequestParam(value = "product3DModel", required = false) MultipartFile product3DModel,
			@RequestParam(value = "assetBundle", required = false) MultipartFile assetBundle,
			@RequestParam(value = "tags", required = false) String[] tags,
			@RequestParam("image") List<MultipartFile> images
			) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Product product = new Product();
		try {
			product.setProductId(0);
			product.setName(name);
			product.setDisplayType(displayType);
			product.setDescription(description);
			product.setBrand(brand);
			Optional<RoomType> result = this.roomTypeRepo.findById(Integer.valueOf(5));
			RoomType theRoom = null;
			if (result.isPresent()) {
				theRoom = result.get();
			} else {
				throw new RuntimeException("Did not find room id - 5");
			}
			product.setRoomType(theRoom);
			product.setFabric(fabric);
			product.setColour(color);
			product.setFrame(primaryMaterial);
			product.setPurchaseNote(purchaseNote);
			product.setHeight(Float.valueOf(height));
			product.setpLength(Float.valueOf(pLength));
			product.setWidth(Float.valueOf(width));
			product.setWeight(Float.valueOf(weight));
			product.setMaterialInfo(materialInfo);
			product.setPrice(price);
			product.setCatalogId(1);
			product.setStock(Integer.valueOf(stock));
			List<ProductCategory> categoryIdList = new ArrayList<>();
			for (int i = 0; i < categoryIds.length; i++) {
				Optional<ProductCategory> result2 = this.productCategoryRepository
						.findById(Integer.valueOf(Integer.parseInt(categoryIds[i])));
				ProductCategory productCat = null;
				if (result2.isPresent())
					productCat = result2.get();
				this.productCategoryRepository.save(productCat);
				categoryIdList.add(productCat);
			}
			product.setCategories(categoryIdList);
			List<Subcategory> subcategoryList = new ArrayList<>();
			if (subcategoryIds != null)
				for (int k = 0; k < subcategoryIds.length; k++) {
					Optional<Subcategory> result1 = this.subcategoryRepo
							.findById(Integer.valueOf(Integer.parseInt(categoryIds[k])));
					Subcategory subCat = null;
					if (result1.isPresent())
						subCat = result1.get();
					this.subcategoryRepo.save(subCat);
					subcategoryList.add(subCat);
				}
			product.setSubCategories(subcategoryList);
			List<Tag> tagList = new ArrayList<>();
			for (int j = 0; j < tags.length; j++) {
				Tag tag = new Tag();
				tag.setTagName(tags[j]);
				this.tagRepository.save(tag);
				tagList.add(tag);
			}
			product.setTags(tagList);
			String thumbnailImageURL = uplodaToAzureStorage(thumbnailImage);
			product.setThumbnailImageURL(thumbnailImageURL);
			if (assetBundle != null) {
				String assetBundleURL = uplodaToAzureStorage(assetBundle);
				product.setAssetBundle(assetBundleURL);
			} else {
				product.setAssetBundle(null);
			}
			if (product3DModel != null) {
				String product3DModelUrl = uplodaToAzureStorage(product3DModel);
				product.setProduct3DModelUrl(product3DModelUrl);
			} else {
				product.setProduct3DModelUrl(null);
			}
			product.setUploadStatus(Integer.valueOf(5));
			product.setCreatedOn(timestamp);
			product.setUpdatedOn(timestamp);
			product.setCreatedBy(1);
			product.setUpdatedBy(1);
			product.setIsDeleted(Boolean.valueOf(false));
			Product theProduct = this.productService.addProduct(product);
			for (MultipartFile image : images) {
				ProductImage productImage = new ProductImage();
				String productImageurl = uplodaToAzureStorage(image);
				productImage.setImageId(0);
				productImage.setImageUrl(productImageurl);
				productImage.setProduct(theProduct);
				productImage.setCreatedBy(1);
				productImage.setUpdatedBy(1);
				productImage.setCreatedOn(timestamp);
				productImage.setUpdatedOn(timestamp);
				productImage.setIsDeleted(false);
				
				productImageRepo.save(productImage);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity("Product added successfully", HttpStatus.OK);
	}
  
  @PutMapping({"/update"})
	public ResponseEntity<Object> updateProduct(@RequestParam("productId") int productId,
			@RequestParam("categoryIds") String[] categoryIds,
			@RequestParam(value = "subcategoryIds", required = false) String[] subcategoryIds,
			@RequestParam("name") String name, @RequestParam("displayType") String displayType,
			@RequestParam("description") String description, @RequestParam("brand") String brand,
			@RequestParam("roomType") int roomType, @RequestParam(value = "fabric", required = false) String fabric,
			@RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "primaryMaterial", required = false) String frame,
			@RequestParam(value = "purchaseNote", required = false) String purchaseNote,
			@RequestParam("height") float height, @RequestParam("plength") float pLength,
			@RequestParam("width") float width, @RequestParam("weight") float weight,
			@RequestParam("materialInfo") String materialInfo, @RequestParam("price") float price,
			@RequestParam("stock") int stock, 
			@RequestParam(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
			@RequestParam(value = "product3DModel", required = false) MultipartFile product3DModel,
			@RequestParam(value = "assetBundle", required = false) MultipartFile assetBundle,
			@RequestParam(value = "tags", required = false) String[] tags,
			@RequestParam("image") List<MultipartFile> images,
			@RequestParam(value = "imageId", required = false) int[] imageIds) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Optional<Product> result = this.productRepository.findById(Integer.valueOf(productId));
		Product product = null;
		if (result.isPresent())
			product = result.get();
		try {
			product.setName(name);
			product.setDisplayType(displayType);
			product.setDescription(description);
			product.setBrand(brand);
			Optional<RoomType> result2 = this.roomTypeRepo.findById(Integer.valueOf(5));
			RoomType theRoom = null;
			if (result2.isPresent()) {
				theRoom = result2.get();
			} else {
				throw new RuntimeException("Did not find room id - 5");
			}
			product.setRoomType(theRoom);
			product.setFabric(fabric);
			product.setColour(color);
			product.setFrame(frame);
			product.setPurchaseNote(purchaseNote);
			product.setHeight(Float.valueOf(height));
			product.setpLength(Float.valueOf(pLength));
			product.setWidth(Float.valueOf(width));
			product.setWeight(Float.valueOf(weight));
			product.setMaterialInfo(materialInfo);
			product.setPrice(price);
			product.setCatalogId(1);
			product.setStock(Integer.valueOf(stock));
			List<ProductCategory> categoryIdList = new ArrayList<>();
			for (int i = 0; i < categoryIds.length; i++) {
				Optional<ProductCategory> resultCat = this.productCategoryRepository
						.findById(Integer.valueOf(Integer.parseInt(categoryIds[i])));
				ProductCategory productCat = null;
				if (result.isPresent())
					productCat = resultCat.get();
				this.productCategoryRepository.save(productCat);
				categoryIdList.add(productCat);
			}
			product.setCategories(categoryIdList);
			List<Subcategory> subcategoryList = new ArrayList<>();
			if (subcategoryIds != null)
				for (int k = 0; k < subcategoryIds.length; k++) {
					Optional<Subcategory> resultSubCat = this.subcategoryRepo
							.findById(Integer.valueOf(Integer.parseInt(categoryIds[k])));
					Subcategory subCat = null;
					if (result.isPresent())
						subCat = resultSubCat.get();
					this.subcategoryRepo.save(subCat);
					subcategoryList.add(subCat);
				}
			product.setSubCategories(subcategoryList);
			List<Tag> tagList = new ArrayList<>();
			for (int j = 0; j < tags.length; j++) {
				Tag tag = new Tag();
				tag.setTagName(tags[j]);
				this.tagRepository.save(tag);
				tagList.add(tag);
			}
			product.setTags(tagList);
			
			if(thumbnailImage != null) {
				String thumbnailImageURL = uplodaToAzureStorage(thumbnailImage);
				product.setThumbnailImageURL(thumbnailImageURL);
			}
			
			if (assetBundle != null) {
				String assetBundleURL = uplodaToAzureStorage(assetBundle);
				product.setAssetBundle(assetBundleURL);
			} else {
				product.setAssetBundle(null);
			}
			
			if(product3DModel != null) {
				
				String product3DModelUrl = uplodaToAzureStorage(product3DModel);
				product.setProduct3DModelUrl(product3DModelUrl);
			}
			
			product.setCreatedOn(timestamp);
			product.setUpdatedOn(timestamp);
			product.setCreatedBy(1);
			product.setUpdatedBy(1);
			product.setIsDeleted(Boolean.valueOf(false));
			Product theProduct = this.productService.addProduct(product);
			
			
			
			if (imageIds != null)
				for (int id : imageIds) {
					productImgSvc.deleteProductImageById(Integer.valueOf(id));
				}
					
			
			for (MultipartFile image : images) {
				ProductImage productImage = new ProductImage();
				String productImageurl = uplodaToAzureStorage(image);
				productImage.setImageId(0);
				productImage.setImageUrl(productImageurl);
				productImage.setProduct(theProduct);
				productImage.setCreatedBy(1);
				productImage.setUpdatedBy(1);
				productImage.setCreatedOn(timestamp);
				productImage.setUpdatedOn(timestamp);
				productImage.setIsDeleted(false);
				this.productImageRepo.save(productImage);
			}
			
			
		} catch (Exception e) {
			
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity("Product updated successfully", HttpStatus.OK);
	}
  
  @DeleteMapping({"/delete/{productId}"})
  public ResponseEntity<Object> deleteProduct(@PathVariable int productId) {
    try {
      Product product = this.productService.findById(productId);
      if (product == null)
        throw new RuntimeException("Product id not found - " + productId); 
      this.productService.deleteProduct(productId);
      return new ResponseEntity("Delete success!", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
  }
  
  @GetMapping({"/getById/{productId}"})
  public ResponseEntity<Object> getById(@PathVariable int productId) {
    try {
      Product theProduct = this.productService.findById(productId);
      if (theProduct == null)
        throw new RuntimeException("Product id not found - " + productId); 
      return new ResponseEntity(theProduct, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
  }
  
  @GetMapping({"/list"})
  //@PreAuthorize()
  public ResponseEntity<Object> getAll() {
    try {
      List<Product> productList = this.productService.findAll();
      return new ResponseEntity(productList, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
  }
  
  @PutMapping({"/updateStatus"})
  public ResponseEntity<Object> updateProductStatus(@RequestParam("productId") int productId, @RequestParam("statusId") int statusId) {
    this.productService.updateProductStatus(productId, statusId);
    return new ResponseEntity("Product updated successfully", HttpStatus.OK);
  }
  
  @PutMapping({"/addAssetBundle"})
	public String addAssetBundle(
			@RequestParam("productId") int productId,
			@RequestParam("assetBundle") MultipartFile assetBundle) {
		String assetBundleURL = uplodaToAzureStorage(assetBundle);
		Optional<Product> result = this.productRepository.findById(Integer.valueOf(productId));
		Product theProduct = null;
		if (result.isPresent()) {
			theProduct = result.get();
		} else {
			throw new RuntimeException("Did not find product id - " + productId);
		}
		theProduct.setAssetBundle(assetBundleURL);
		this.productRepository.save(theProduct);
		this.productService.updateProductStatus(productId, 4);
		return "Asset bundle for productId: " + productId + " uploaded successful!!";
	}
  
  @GetMapping({"/imagesList"})
  public ResponseEntity<Object> productImages(@RequestParam("categoryId") int categoryId) {
    List<JSONObject> entities = new ArrayList<>();
    entities = this.productRepository.findImagesByCategory(Integer.valueOf(categoryId));
    return new ResponseEntity(entities, HttpStatus.OK);
  }
  
  @GetMapping({"/thumbnailImagesAndAssetBundle"})
  public ResponseEntity<Object> productThumbnailImagesAndAssetBundle(@RequestParam("categoryId") int categoryId) {
    List<JSONObject> entities = new ArrayList<>();
    entities = this.productRepository.findThumnailAndAssetBundleByCategory(Integer.valueOf(categoryId));
    return new ResponseEntity(entities, HttpStatus.OK);
  }
  
  @GetMapping({"/categoryList"})
  public ResponseEntity<Object> productCategoryList() {
    List<JSONObject> entities = new ArrayList<>();
    entities = this.productCategoryRepository.categoryList();
    return new ResponseEntity(entities, HttpStatus.OK);
  }
  
  @GetMapping({"/getImagesById/{productId}"})
  public ResponseEntity<Object> findImagesByProjectId(@PathVariable("productId") Integer productId) {
    try {
      List<JSONObject> entities = new ArrayList<>();
      entities = this.productImgSvc.findImagesByProductId(productId);
      return new ResponseEntity(entities, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } 
  }
  
//  @DeleteMapping({"/deleteImagesByProductId/{productId}"})
//  public ResponseEntity<Object> deleteImagesByProductId(@Param("productId") Integer productId) {
//    try {
//      this.productImgSvc.deleteImagesByProductId(productId);
//      return new ResponseEntity("Delete successful", HttpStatus.OK);
//    } catch (Exception e) {
//      return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    } 
//  }
  
  public String uplodaToAzureStorage(MultipartFile file) {
    CloudBlockBlob blob = null;
    try {
      CloudBlobClient blobClient = this.cloudStorageAccount.createCloudBlobClient();
      CloudBlobContainer container = blobClient.getContainerReference("product");
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
