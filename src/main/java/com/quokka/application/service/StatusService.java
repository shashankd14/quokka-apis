package com.quokka.application.service;

import com.quokka.application.entity.Status;
import java.util.List;

public interface StatusService {
  List<Status> findAll();
}
