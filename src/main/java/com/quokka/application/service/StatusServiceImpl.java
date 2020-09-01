package com.quokka.application.service;

import com.quokka.application.dao.StatusRepository;
import com.quokka.application.entity.Status;
import com.quokka.application.service.StatusService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {
  @Autowired
  private StatusRepository statusRepo;
  
  public List<Status> findAll() {
    return this.statusRepo.findAll();
  }
}
