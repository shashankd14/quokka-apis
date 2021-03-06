package com.quokka.application.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "status")
public class Status {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "statusid")
  private int statusId;
  
  @Column(name = "statusname")
  private String statusName;
  
  @OneToMany(mappedBy = "stock")
  @JsonBackReference
  private List<Product> product;
  
  public int getStatusId() {
    return this.statusId;
  }
  
  public void setStatusId(int statusId) {
    this.statusId = statusId;
  }
  
  public String getStatusName() {
    return this.statusName;
  }
  
  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }
}
