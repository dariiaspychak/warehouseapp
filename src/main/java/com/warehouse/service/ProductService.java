package com.warehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.warehouse.dao.ProductDao;
import com.warehouse.object.external.ExternalProduct;
import com.warehouse.object.internal.ConversionUtility;

@Component
public class ProductService {
	@Autowired
	private ProductDao productDao;
	
	public ExternalProduct createProduct(String name, String description, int price, boolean isActive){
		return ConversionUtility.convert(productDao.createProduct(name, description, price, isActive));
	}
	
	public void deactivateProduct(int productId){
		productDao.deactivateProduct(productId);
	}
	
	public void activateProduct(int productId){
		productDao.activateProduct(productId);
	}
	
	public ExternalProduct getProduct(int productId){
		return ConversionUtility.convert(productDao.getProduct(productId));
	}
	
	public ExternalProduct getActiveProduct(int productId){
		return ConversionUtility.convert(productDao.getActiveProduct(productId));
	}
}
