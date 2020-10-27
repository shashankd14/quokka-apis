 package com.quokka.application.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.quokka.application.dao.ProductCategoryRepository;
import com.quokka.application.dao.ProductImageRepository;
import com.quokka.application.dao.ProductRepository;
import com.quokka.application.dao.RoomTypeRepository;
import com.quokka.application.dao.StatusRepository;
import com.quokka.application.dao.SubCategoryRepository;
import com.quokka.application.dao.TagRepository;
import com.quokka.application.entity.Product;
import com.quokka.application.entity.ProductCategory;
import com.quokka.application.entity.ProductImage;
import com.quokka.application.entity.RoomType;
import com.quokka.application.entity.Status;
import com.quokka.application.entity.Subcategory;
import com.quokka.application.entity.Tag;
import com.quokka.application.service.ProductImageService;
import com.quokka.application.service.ProductService;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping({"/api/product"})
public class ProductController {
  @Autowired
  private ProductService productService;
  
  @Autowired
  private ProductRepository productRepository;
  
  @Autowired
  private TagRepository tagRepository;
  
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
  
  @Autowired
  private StatusRepository statusRepo;
  
  @PostMapping({"/add"})
	public ResponseEntity<Object> addProduct(
			@RequestParam("categoryIds") String[] categoryIds,
			@RequestParam(value = "subcategoryIds", required = false) String[] subcategoryIds,
			@RequestParam("name") String name, 
			@RequestParam(value = "displayType", required = false) String displayType,
			@RequestParam(value = "description", required = false) String description, 
			@RequestParam(value = "brand", required = false) String brand,
			@RequestParam(value = "roomType", required = false) Integer roomType, 
			@RequestParam(value = "fabric", required = false) String fabric,
			@RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "primaryMaterial", required = false) String primaryMaterial,
			@RequestParam(value = "purchaseNote", required = false) String purchaseNote,
			@RequestParam(value = "height", required = false) Float height, 
			@RequestParam(value = "plength", required = false) Float pLength,
			@RequestParam(value = "width", required = false) Float width, 
			@RequestParam(value = "weight", required = false) Float weight,
			//@RequestParam("materialInfo") String materialInfo, 
			@RequestParam(value = "price", required = false) Float price,
			@RequestParam(value = "stock", required = false) Integer stock, 
			@RequestParam("thumbnailImage") MultipartFile thumbnailImage,
			@RequestParam(value = "product3DModel", required = false) MultipartFile product3DModel,
			@RequestParam(value = "assetBundle", required = false) MultipartFile assetBundle,
			@RequestParam(value = "tags", required = false) String[] tags,
			@RequestParam(value = "image", required = false) List<MultipartFile> images,
			@RequestParam("userId") int userId
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
			product.setPrimaryMaterial(primaryMaterial);
			product.setPurchaseNote(purchaseNote);
			
			if(height != null)
			product.setHeight(Float.valueOf(height));
			
			if(pLength != null)
			product.setpLength(Float.valueOf(pLength));
			
			if(width != null)
			product.setWidth(Float.valueOf(width));
			
			if(weight != null)
			product.setWeight(Float.valueOf(weight));
			
			if(price != null)
			product.setPrice(price);
			product.setCatalogId(1);
			
			if(stock != null) {
				
				Optional<Status> resultStatus = statusRepo.findById(stock);
				Status theStatus = null;
				if (resultStatus.isPresent()) {
					theStatus = resultStatus.get();
				} else {
					throw new RuntimeException("Did not find status id ");
				}
				product.setStock(theStatus);
			}
			
		//	product.setRoomType(theRoom);
			
			
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
			if(tags!=null) {
				
				for (int j = 0; j < tags.length; j++) {
					Tag tag = new Tag();
					tag.setTagName(tags[j]);
					this.tagRepository.save(tag);
					tagList.add(tag);
				}
			}
			
			product.setTags(tagList);
			String thumbnailImageURL = uploadToBucket(thumbnailImage);
			product.setThumbnailImageURL(thumbnailImageURL);
			if (assetBundle != null) {
				String assetBundleURL = uploadToBucket(assetBundle);
				product.setAssetBundle(assetBundleURL);
			} else {
				product.setAssetBundle(null);
			}
			if (product3DModel != null) {
				String product3DModelUrl = uploadToBucket(product3DModel);
				product.setProduct3DModelUrl(product3DModelUrl);
			} else {
				product.setProduct3DModelUrl(null);
			}
			product.setUploadStatus(Integer.valueOf(5));
			product.setCreatedOn(timestamp);
			product.setUpdatedOn(timestamp);
			product.setCreatedBy(userId);
			product.setUpdatedBy(userId);
			product.setIsDeleted(Boolean.valueOf(false));
			Product theProduct = this.productService.addProduct(product);
			for (MultipartFile image : images) {
				ProductImage productImage = new ProductImage();
				String productImageurl = uploadToBucket(image);
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
			@RequestParam("name") String name, 
			@RequestParam(value = "displayType", required = false) String displayType,
			@RequestParam("description") String description, @RequestParam("brand") String brand,
			@RequestParam("roomType") int roomType, @RequestParam(value = "fabric", required = false) String fabric,
			@RequestParam(value = "color", required = false) String color,
			@RequestParam(value = "primaryMaterial", required = false) String primaryMaterial,
			@RequestParam(value = "purchaseNote", required = false) String purchaseNote,
			@RequestParam(value = "height", required = false) float height, 
			@RequestParam(value = "plength", required = false) float pLength,
			@RequestParam(value = "width", required = false) float width, 
			@RequestParam(value = "weight", required = false) float weight,
			//@RequestParam("materialInfo") String materialInfo, 
			@RequestParam("price") float price,
			@RequestParam("stock") int stock, 
			@RequestParam(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
			@RequestParam(value = "product3DModel", required = false) MultipartFile product3DModel,
			@RequestParam(value = "assetBundle", required = false) MultipartFile assetBundle,
			@RequestParam(value = "tags", required = false) String[] tags,
			@RequestParam(value = "image", required = false) List<MultipartFile> images,
			@RequestParam(value = "imageId", required = false) int[] imageIds,
			@RequestParam("userId") int userId) {
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
			product.setPrimaryMaterial(primaryMaterial);
			product.setPurchaseNote(purchaseNote);
			product.setHeight(Float.valueOf(height));
			product.setpLength(Float.valueOf(pLength));
			product.setWidth(Float.valueOf(width));
			product.setWeight(Float.valueOf(weight));
			//product.setMaterialInfo(materialInfo);
			product.setPrice(price);
			product.setCatalogId(1);
			
			
			Optional<Status> resultStatus = statusRepo.findById(stock);
			Status theStatus = null;
			if (resultStatus.isPresent()) {
				theStatus = resultStatus.get();
			} else {
				throw new RuntimeException("Did not find status id ");
			}
		//	product.setRoomType(theRoom);
			
			product.setStock(theStatus);
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
				String thumbnailImageURL = uploadToBucket(thumbnailImage);
				product.setThumbnailImageURL(thumbnailImageURL);
			}
			
			if (assetBundle != null) {
				String assetBundleURL = uploadToBucket(assetBundle);
				product.setAssetBundle(assetBundleURL);
			} else {
				product.setAssetBundle(null);
			}
			
			if(product3DModel != null) {
				
				String product3DModelUrl = uploadToBucket(product3DModel);
				product.setProduct3DModelUrl(product3DModelUrl);
			}
			
			//product.setCreatedOn(timestamp);
			product.setUpdatedOn(timestamp);
			//product.setCreatedBy(1);
			product.setUpdatedBy(userId);
			product.setIsDeleted(Boolean.valueOf(false));
			Product theProduct = this.productService.addProduct(product);
			
			
			
			if (imageIds != null)
				for (int id : imageIds) {
					productImgSvc.deleteProductImageById(Integer.valueOf(id));
				}
					
			
			for (MultipartFile image : images) {
				ProductImage productImage = new ProductImage();
				String productImageurl = uploadToBucket(image);
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
  public ResponseEntity<Object> getAll(@RequestParam("manufacturerId") int userId) {
		try {

			int[] adminArray = { 83, 84, 85 };

			List<Product> productList = new ArrayList<Product>();
			if (Arrays.stream(adminArray).anyMatch(i -> i == userId)) {

				productList = productService.getAdminProductList();
			} else {
				productList = this.productService.findAll(userId);
			}
			return new ResponseEntity<Object>(productList, HttpStatus.OK);
		} catch (Exception e) {
      return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
		String assetBundleURL = uploadToBucket(assetBundle);
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
  
	
	
	public String uploadToBucket(@RequestParam("file") MultipartFile file) {

		String uploadUrl = "";
		try {
			String projectId = "quokka-project-alpha";
			String bucketName = "quokka-product-uploads";
			String fileName = file.getOriginalFilename();

			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

			BlobId blobId = BlobId.of(bucketName, fileName);

			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

			byte[] bytes = file.getBytes();

			Blob blob = storage.create(blobInfo, bytes);

			uploadUrl = "https://storage.googleapis.com/quokka-product-uploads/" + fileName;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return uploadUrl;
	}
}
