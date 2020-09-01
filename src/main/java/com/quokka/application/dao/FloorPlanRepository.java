package com.quokka.application.dao;

import com.quokka.application.entity.FloorPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorPlanRepository extends JpaRepository<FloorPlan, Integer> {}
