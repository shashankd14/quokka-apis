package com.quokka.application.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.quokka.application.entity.Order;
import com.quokka.application.entity.ProductCategory;
import com.quokka.application.entity.ProductImage;
import com.quokka.application.entity.RoomType;
import com.quokka.application.entity.Subcategory;
import com.quokka.application.entity.Tag;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "productid")
	private int productId;

	@Column(name = "displaytype")
	private String displayType;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "brand")
	private String brand;

	@Column(name = "product3Dmodelurl")
	private String product3DModelUrl;

	@Column(name = "uploadstatus")
	private Integer uploadStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roomtype")
	@JsonManagedReference
	private RoomType roomType;

	@Column(name = "fabric")
	private String fabric;

	@Column(name = "colour")
	private String colour;

	@Column(name = "frame")
	private String frame;

	@Column(name = "purchase_note")
	private String purchaseNote;

	@Column(name = "height")
	private Float height;

	@Column(name = "plength")
	private Float pLength;

	@Column(name = "width")
	private Float width;

	@Column(name = "weight")
	private Float weight;

	@Column(name = "thumbnailimageurl")
	private String thumbnailImageURL;

	@Column(name = "assetbundle")
	private String assetBundle;

	@Column(name = "materialinfo")
	private String materialInfo;

	@Column(name = "price")
	private double price;

	@Column(name = "catalogid")
	private int catalogId;

	@Column(name = "stock")
	private Integer stock;

	@Column(name = "createdby")
	private int createdBy;

	@Column(name = "updatedby")
	private int updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdon")
	private Date createdOn;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedon")
	private Date updatedOn;

	@Column(name = "isdeleted", columnDefinition = "BIT")
	private Boolean isDeleted;

	@OneToMany(mappedBy="product")
	@JsonBackReference
	private List<ProductImage> images;

	
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
			CascadeType.REFRESH })
	@JoinTable(name = "product_tag", joinColumns = { @JoinColumn(name = "productid") }, inverseJoinColumns = {
			@JoinColumn(name = "tagid") })
	@JsonManagedReference
	private List<Tag> tags;

	@JsonManagedReference
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "product_category_jt", joinColumns = { @JoinColumn(name = "productid") }, inverseJoinColumns = {
			@JoinColumn(name = "categoryid") })
	private List<ProductCategory> categories;

	@JsonManagedReference
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
			CascadeType.REFRESH })
	@JoinTable(name = "product_subcategory_jt", joinColumns = {
			@JoinColumn(name = "productid") }, inverseJoinColumns = { @JoinColumn(name = "subcategoryid") })
	private List<Subcategory> subCategories;

	public List<ProductImage> getImages() {
		return this.images;
	}

	public void setImages(List<ProductImage> images) {
		this.images = images;
	}

	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getDisplayType() {
		return this.displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getProduct3DModelUrl() {
		return this.product3DModelUrl;
	}

	public void setProduct3DModelUrl(String product3dModelUrl) {
		this.product3DModelUrl = product3dModelUrl;
	}

	public Integer getUploadStatus() {
		return this.uploadStatus;
	}

	public void setUploadStatus(Integer uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public RoomType getRoomType() {
		return this.roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public Float getHeight() {
		return this.height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getpLength() {
		return this.pLength;
	}

	public void setpLength(Float pLength) {
		this.pLength = pLength;
	}

	public Float getWidth() {
		return this.width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Float getWeight() {
		return this.weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getThumbnailImageURL() {
		return this.thumbnailImageURL;
	}

	public void setThumbnailImageURL(String thumbnailImageURL) {
		this.thumbnailImageURL = thumbnailImageURL;
	}

	public String getAssetBundle() {
		return this.assetBundle;
	}

	public void setAssetBundle(String assetBundle) {
		this.assetBundle = assetBundle;
	}

	public String getMaterialInfo() {
		return this.materialInfo;
	}

	public void setMaterialInfo(String materialInfo) {
		this.materialInfo = materialInfo;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCatalogId() {
		return this.catalogId;
	}

	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}

	public Integer getStock() {
		return this.stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<Tag> getTags() {
		return this.tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public String getFabric() {
		return this.fabric;
	}

	public void setFabric(String fabric) {
		this.fabric = fabric;
	}

	public String getColour() {
		return this.colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getFrame() {
		return this.frame;
	}

	public void setFrame(String frame) {
		this.frame = frame;
	}

	public String getPurchaseNote() {
		return this.purchaseNote;
	}

	public void setPurchaseNote(String purchaseNote) {
		this.purchaseNote = purchaseNote;
	}

	public List<ProductCategory> getCategories() {
		return this.categories;
	}

	public void setCategories(List<ProductCategory> categories) {
		this.categories = categories;
	}

	public List<Subcategory> getSubCategories() {
		return this.subCategories;
	}

	public void setSubCategories(List<Subcategory> subCategories) {
		this.subCategories = subCategories;
	}
}
