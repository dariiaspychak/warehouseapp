package com.warehouse.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.warehouse.dao.OrderDao;
import com.warehouse.dao.ProductDao;
import com.warehouse.object.external.ExternalOrder;
import com.warehouse.object.external.ExternalOrderStatus;
import com.warehouse.object.external.ExternalProduct;
import com.warehouse.object.internal.Order;
import com.warehouse.object.internal.OrderChangeRequest;
import com.warehouse.object.internal.OrderStatus;
import com.warehouse.object.internal.Product;

@Component
public class Service {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ProductDao productDao;

	public ExternalOrder createOrder() {
		return convertInternalToExternal(orderDao.createOrder());
	}

	public ExternalOrder changeOrder(int orderId, OrderChangeRequest orderChangeRequest) {
		return convertInternalToExternal(orderDao.changeOrder(orderId, orderChangeRequest));
	}
	
	public ExternalOrder submitOrder(int orderId){
		return convertInternalToExternal(orderDao.submitOrder(orderId));	
	}
	
	public ExternalOrder getOrderById(int orderId){
		return convertInternalToExternal(orderDao.getOrderById(orderId));
	}
	
	public List<ExternalOrder> getOrderByStatus(ExternalOrderStatus externalOrderStatus){
		List<Order> orderList = orderDao.getOrderByStatus(OrderStatus.convert(externalOrderStatus));
		List<ExternalOrder> externalOrderList = new ArrayList<ExternalOrder>();
		for(Order order: orderList){
			externalOrderList.add(convertInternalToExternal(order));
		}
		return externalOrderList;
	}
		
	private ExternalOrder convertInternalToExternal(Order order){
		ExternalOrder externalOrder = new ExternalOrder();
		externalOrder.setOrderId(order.getId());
		externalOrder.setOrderStatus(ExternalOrderStatus.convert(order.getOrderStatus()));
		externalOrder.setProducts(new HashMap<ExternalProduct, Integer>());
		return externalOrder;
	}
	
	private ExternalProduct convertInternalToExternalProduct(Product product){
		ExternalProduct externalProduct = new ExternalProduct();
		externalProduct.setId(product.getProductId());
		externalProduct.setDescription(product.getDescription());
		externalProduct.setName(product.getName());
		externalProduct.setPrice(product.getPrice());
		externalProduct.setActive(product.isActive());
		return externalProduct;
	}
	
	public ExternalProduct addNewProduct(String name, String description, int price, boolean isActive){
		return convertInternalToExternalProduct(productDao.addNewProduct(name, description, price, isActive));
	}
	
	public void deactivateProduct(int productId){
		productDao.deactivateProduct(productId);
	}
	
	public void activateProduct(int productId){
		productDao.activateProduct(productId);
	}
	
	public ExternalProduct getProductById(int productId){
		return convertInternalToExternalProduct(productDao.getProductById(productId));
	}
	
	public ExternalProduct getActiveProductById(int productId){
		return convertInternalToExternalProduct(productDao.getActiveProductById(productId));
	}
}
