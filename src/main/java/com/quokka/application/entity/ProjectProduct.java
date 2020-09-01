package com.quokka.application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "project_products")
public class ProjectProduct {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "project_product_id")
  int projectProductId;
  
  @Column(name = "projectid")
  int projectId;
  
  @Column(name = "productid")
  int productId;
  
  @Column(name = "variant")
  String variant;
  
  @Column(name = "spatialanchorid")
  String spatialAnchorID;
  
  @Column(name = "positionx")
  float positionX;
  
  @Column(name = "positiony")
  float positionY;
  
  @Column(name = "positionz")
  float positionZ;
  
  @Column(name = "rotationX")
  float rotationX;
  
  @Column(name = "rotationy")
  float rotationY;
  
  @Column(name = "rotationz")
  float rotationZ;
  
  @Column(name = "scalex")
  float scaleX;
  
  @Column(name = "scaley")
  float scaleY;
  
  @Column(name = "scalez")
  float scaleZ;
  
  public int getProjectProductId() {
    return this.projectProductId;
  }
  
  public void setProjectProductId(int projectProductId) {
    this.projectProductId = projectProductId;
  }
  
  public int getProjectId() {
    return this.projectId;
  }
  
  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }
  
  public int getProductId() {
    return this.productId;
  }
  
  public void setProductId(int productId) {
    this.productId = productId;
  }
  
  public String getVariant() {
    return this.variant;
  }
  
  public void setVariant(String variant) {
    this.variant = variant;
  }
  
  public String getSpatialAnchorID() {
    return this.spatialAnchorID;
  }
  
  public void setSpatialAnchorID(String spatialAnchorID) {
    this.spatialAnchorID = spatialAnchorID;
  }
  
  public float getPositionX() {
    return this.positionX;
  }
  
  public void setPositionX(float positionX) {
    this.positionX = positionX;
  }
  
  public float getPositionY() {
    return this.positionY;
  }
  
  public void setPositionY(float positionY) {
    this.positionY = positionY;
  }
  
  public float getPositionZ() {
    return this.positionZ;
  }
  
  public void setPositionZ(float positionZ) {
    this.positionZ = positionZ;
  }
  
  public float getRotationX() {
    return this.rotationX;
  }
  
  public void setRotationX(float rotationX) {
    this.rotationX = rotationX;
  }
  
  public float getRotationY() {
    return this.rotationY;
  }
  
  public void setRotationY(float rotationY) {
    this.rotationY = rotationY;
  }
  
  public float getRotationZ() {
    return this.rotationZ;
  }
  
  public void setRotationZ(float rotationZ) {
    this.rotationZ = rotationZ;
  }
  
  public float getScaleX() {
    return this.scaleX;
  }
  
  public void setScaleX(float scaleX) {
    this.scaleX = scaleX;
  }
  
  public float getScaleY() {
    return this.scaleY;
  }
  
  public void setScaleY(float scaleY) {
    this.scaleY = scaleY;
  }
  
  public float getScaleZ() {
    return this.scaleZ;
  }
  
  public void setScaleZ(float scaleZ) {
    this.scaleZ = scaleZ;
  }
}
