package com.quokka.application.service;

import com.quokka.application.dao.ProductRepository;
import com.quokka.application.entity.Product;
import com.quokka.application.service.ProductService;
import java.util.List;
import java.util.Optional;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
	private ProductRepository productRepo;

	public ProductServiceImpl(ProductRepository theProductRepo) {
		this.productRepo = theProductRepo;
	}

	public List<JSONObject[]> getAllProductsByProjectIdUI(int projectId) {
		return this.productRepo.getAllProductsByProjectIdUI(projectId);
	}

	public Product addProduct(Product product) {
		return (Product) this.productRepo.save(product);
	}

	public Product addAssetBundle(int productId, String assetBundleURL) {
		return this.productRepo.addAssetBundle(Integer.valueOf(productId), assetBundleURL);
	}

	public Product updateProduct(Product product) {
		return (Product) this.productRepo.save(product);
	}

	public void deleteProduct(int productId) {
		this.productRepo.deleteByProductId(productId);
	}

	public Product findById(int productId) {
		Optional<Product> result = this.productRepo.findById(Integer.valueOf(productId));
		Product theProduct = null;
		if (result.isPresent()) {
			theProduct = result.get();
		} else {
			throw new RuntimeException("Did not find theProduct id - " + productId);
		}
		return theProduct;
	}

	public List<Product> findAll() {
		return this.productRepo.findAll();
	}

	public void updateProductStatus(int productId, int statusId) {
		this.productRepo.updateProductStatus(productId, statusId);
	}
}
