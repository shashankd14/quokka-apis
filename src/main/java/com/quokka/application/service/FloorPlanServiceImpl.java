package com.quokka.application.service;

import com.quokka.application.dao.FloorPlanRepository;
import com.quokka.application.service.FloorPlanService;
import org.springframework.beans.factory.annotation.Autowired;

public class FloorPlanServiceImpl implements FloorPlanService {
  @Autowired
  private FloorPlanRepository flrPlnRepo;
}
