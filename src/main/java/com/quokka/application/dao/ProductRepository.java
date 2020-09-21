package com.quokka.application.dao;

import com.quokka.application.entity.Product;
import java.util.List;
import javax.transaction.Transactional;
import net.minidev.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query(nativeQuery = true, value = "SELECT imageUrl  FROM images i  "
			+ "	LEFT JOIN products p  ON i.productId = p.productId  WHERE p.categoryId= :categoryId  ")
	public List<JSONObject> findImagesByCategory(@Param("categoryId") Integer categoryId);

	@Query(nativeQuery = true, value = "select p.productId, p.thumbnailImageURL, p.assetBundle from products p  left join product_category_jt pc on pc.productId = p.productId where pc.categoryId=:categoryId")
	public List<JSONObject> findThumnailAndAssetBundleByCategory(@Param("categoryId") Integer categoryId);

	@Query(nativeQuery = true, value = "select products.name, products.description, products.thumbnailImageURL from products "
			+ "LEFT JOIN project_products  ON  project_products.productId = products.productId "
			+ "where project_products.projectId = :projectId")
	public List<JSONObject[]> getAllProductsByProjectIdUI(@Param("projectId")Integer projectId);

	@Query(nativeQuery = true, value = "update products set assetBundle = ':assetBundleURL' where productId = :productId")
	public Product addAssetBundle(@Param("productId") Integer productId, @Param("assetBundleURL") String assetBundleURL);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update products set uploadStatus = :statusId where productId = :productId")
	public void updateProductStatus(@Param("productId") int productId, @Param("statusId") int statusId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "delete from products where productId = :productId")
	public void deleteByProductId(@Param("productId") int productId);
	
	@Query(nativeQuery = true, value = "select * from products where createdBy= :userId")
	public  List<Product> getProductsByManufacturerId(@Param("userId") int userId);
}
