package com.warehouse.object.internal;

import java.util.HashMap;

import com.warehouse.object.external.ExternalOrder;
import com.warehouse.object.external.ExternalOrderStatus;
import com.warehouse.object.external.ExternalProduct;

public class ConversionUtility {
	
public static ExternalOrder convert(Order order){
	ExternalOrder externalOrder = new ExternalOrder();
	externalOrder.setOrderId(order.getId());
	externalOrder.setOrderStatus(ExternalOrderStatus.convert(order.getOrderStatus()));
	externalOrder.setProducts(new HashMap<ExternalProduct, Integer>());
	return externalOrder;
}

public static ExternalProduct convert(Product product){
	ExternalProduct externalProduct = new ExternalProduct();
	externalProduct.setId(product.getProductId());
	externalProduct.setDescription(product.getDescription());
	externalProduct.setName(product.getName());
	externalProduct.setPrice(product.getPrice());
	externalProduct.setActive(product.isActive());
	return externalProduct;
}
}
