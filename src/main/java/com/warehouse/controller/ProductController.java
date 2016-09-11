package com.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.object.external.ExternalProduct;
import com.warehouse.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/product/new", method = RequestMethod.POST)
	public ExternalProduct createProduct(@RequestBody ExternalProduct externalProduct) {
		return productService.createProduct(externalProduct.getName(), externalProduct.getDescription(),
				externalProduct.getPrice(), externalProduct.isActive());
	}

	@RequestMapping(value = "/product/activate/{productId}", method = RequestMethod.POST)
	public void activateProduct(@PathVariable("productId") int productId) {
		productService.activateProduct(productId);
	}

	@RequestMapping(value = "/product/deactivate/{productId}", method = RequestMethod.POST)
	public void deactivateProduct(@PathVariable("productId") int productId) {
		productService.deactivateProduct(productId);
	}

	@RequestMapping(value = "/product/id/{productId}", method = RequestMethod.GET)
	public ExternalProduct getProduct(@PathVariable("productId") int productId) {
		return productService.getProduct(productId);
	}
	
	@RequestMapping(value = "/product/active/{productId}", method = RequestMethod.GET)
	public ExternalProduct getActiveProduct(@PathVariable("productId") int productId) {
		return productService.getActiveProduct(productId);
	}
}
