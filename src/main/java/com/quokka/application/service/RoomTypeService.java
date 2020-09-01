package com.quokka.application.service;

import com.quokka.application.entity.RoomType;
import java.util.List;

public interface RoomTypeService {
  List<RoomType> findAll();
  
  RoomType getByid(int paramInt);
}
