package com.quokka.application.service;

import com.quokka.application.entity.Product;
import java.util.List;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;

public interface ProductService {
	
	public List<JSONObject[]> getAllProductsByProjectIdUI(@Param("projectId") int projectId);

	public Product findById(int projectId);

	public Product addProduct(Product product);

	public Product updateProduct(Product product);

	public void deleteProduct(int projectId);

	public Product addAssetBundle(int projectId, String assetBundleUrl);

	public List<Product> findAll(int manufacuturerId);

	public void updateProductStatus(int paramInt1, int paramInt2);
	
	public  List<Product> getAdminProductList();
}
