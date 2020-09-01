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
@Table(name = "tag")
public class Tag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tagid")
  private int tagId;
  
  @Column(name = "tagname")
  private String tagName;
  
  @ManyToMany(mappedBy = "tags")
  @JsonBackReference
  private List<Product> products;
  
  public int getTagId() {
    return this.tagId;
  }
  
  public void setTagId(int tagId) {
    this.tagId = tagId;
  }
  
  public String getTagName() {
    return this.tagName;
  }
  
  public void setTagName(String tagName) {
    this.tagName = tagName;
  }
  
  public List<Product> getProducts() {
    return this.products;
  }
  
  public void setProducts(List<Product> products) {
    this.products = products;
  }
}
