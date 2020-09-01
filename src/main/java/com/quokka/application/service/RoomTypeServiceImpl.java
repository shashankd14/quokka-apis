package com.quokka.application.service;

import com.quokka.application.dao.RoomTypeRepository;
import com.quokka.application.entity.RoomType;
import com.quokka.application.service.RoomTypeService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
  @Autowired
  private RoomTypeRepository roomTypeRepo;
  
  public List<RoomType> findAll() {
    return this.roomTypeRepo.findAll();
  }
  
  public RoomType getByid(int roomtypeId) {
    Optional<RoomType> result1 = this.roomTypeRepo.findById(Integer.valueOf(5));
    RoomType theRoom = null;
    if (result1.isPresent()) {
      theRoom = result1.get();
    } else {
      throw new RuntimeException("Did not find room id - 5");
    } 
    return theRoom;
  }
}
