package com.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.object.external.ExternalProduct;
import com.warehouse.service.Service;

@RestController
public class ProductController {

	@Autowired
	private Service service;

	@RequestMapping(value = "/product/new", method = RequestMethod.POST)
	public ExternalProduct createProduct(@RequestBody ExternalProduct externalProduct) {
		return service.createProduct(externalProduct.getName(), externalProduct.getDescription(),
				externalProduct.getPrice(), externalProduct.isActive());
	}

	@RequestMapping(value = "/product/activate/{productId}", method = RequestMethod.POST)
	public void activateProduct(@PathVariable("productId") int productId) {
		service.activateProduct(productId);
	}

	@RequestMapping(value = "/product/deactivate/{productId}", method = RequestMethod.POST)
	public void deactivateProduct(@PathVariable("productId") int productId) {
		service.deactivateProduct(productId);
	}

	@RequestMapping(value = "/product/id/{productId}", method = RequestMethod.GET)
	public ExternalProduct getProduct(@PathVariable("productId") int productId) {
		return service.getProduct(productId);
	}
}
