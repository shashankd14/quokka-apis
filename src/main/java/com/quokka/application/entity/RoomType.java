package com.quokka.application.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.quokka.application.entity.Product;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "room_type")
public class RoomType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "roomid")
  private int roomId;
  
  @Column(name = "roomname")
  private String roomName;
  
  @OneToMany(mappedBy = "roomType")
  @JsonBackReference
  private List<Product> product;
  
  public int getRoomId() {
    return this.roomId;
  }
  
  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }
  
  public String getRoomName() {
    return this.roomName;
  }
  
  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }
  
  public List<Product> getProduct() {
    return this.product;
  }
  
  public void setProduct(List<Product> product) {
    this.product = product;
  }
}
