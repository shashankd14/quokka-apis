package com.quokka.application.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.quokka.application.entity.Product;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "subcategories")
public class Subcategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "subcategoryid")
  private int subCategoryId;
  
  @Column(name = "categoryid")
  private int categoryId;
  
  @Column(name = "subcategoryname")
  private String subcategoryName;
  
  @ManyToMany(mappedBy = "subCategories")
  @JsonBackReference
  private List<Product> products;
  
  public int getSubCategoryId() {
    return this.subCategoryId;
  }
  
  public void setSubCategoryId(int subCategoryId) {
    this.subCategoryId = subCategoryId;
  }
  
  public int getCategoryId() {
    return this.categoryId;
  }
  
  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }
  
  public String getSubcategoryName() {
    return this.subcategoryName;
  }
  
  public void setSubcategoryName(String subcategoryName) {
    this.subcategoryName = subcategoryName;
  }
  
  public List<Product> getProducts() {
    return this.products;
  }
  
  public void setProducts(List<Product> products) {
    this.products = products;
  }
}
