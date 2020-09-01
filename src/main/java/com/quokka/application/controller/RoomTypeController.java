package com.quokka.application.controller;

import com.quokka.application.entity.RoomType;
import com.quokka.application.service.RoomTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/room"})
public class RoomTypeController {
  @Autowired
  private RoomTypeService roomTypeService;
  
  @GetMapping({"/list"})
  public List<RoomType> roomList() {
    return this.roomTypeService.findAll();
  }
}
