package com.quokka.application.controller;

import com.quokka.application.entity.Status;
import com.quokka.application.service.StatusService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/status"})
public class StatusController {
  @Autowired
  private StatusService statusService;
  
  @GetMapping({"/list"})
  public List<Status> findAll() {
    return this.statusService.findAll();
  }
}
