package com.quokka.application.dao;

import com.quokka.application.entity.ProjectProduct;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectProductRepository extends JpaRepository<ProjectProduct, Integer> {
  @Query(nativeQuery = true, value = "select pp.productId, pp.variant, pp.spatialAnchorID, pp.positionX, pp.positionY, pp.positionZ, pp.rotationX, pp.rotationY, pp.rotationZ, pp.scaleX, pp.scaleY, pp.scaleZ, p.categoryId, p.assetBundle "
  		+ "FROM project_products pp "
  		+ "LEFT JOIN products AS p ON  p.productId = pp.productId AND pp.projectId= :projectId")
  List<JSONObject[]> getAllProductsByProjectIdHL(@Param("projectId") int projectId);
}
