package com.warehouse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.warehouse.dao.OrderDao;
import com.warehouse.dao.ProductDao;
import com.warehouse.object.external.ExternalOrder;
import com.warehouse.object.external.ExternalOrderStatus;
import com.warehouse.object.external.ExternalProduct;
import com.warehouse.object.internal.ConversionUtility;
import com.warehouse.object.internal.Order;
import com.warehouse.object.internal.OrderChangeRequest;
import com.warehouse.object.internal.OrderStatus;

@Component
public class Service {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ProductDao productDao;
	

	public ExternalOrder createOrder() {
		return ConversionUtility.convert(orderDao.createOrder());
	}

	public ExternalOrder changeOrder(int orderId, OrderChangeRequest orderChangeRequest) {
		return ConversionUtility.convert(orderDao.changeOrder(orderId, orderChangeRequest));
	}
	
	public ExternalOrder submitOrder(int orderId){
		return ConversionUtility.convert(orderDao.submitOrder(orderId));	
	}
	
	public ExternalOrder getOrder(int orderId){
		return ConversionUtility.convert(orderDao.getOrder(orderId));
	}
	
	public List<ExternalOrder> getOrders(ExternalOrderStatus externalOrderStatus){
		List<Order> orderList = orderDao.getOrders(OrderStatus.convert(externalOrderStatus));
		List<ExternalOrder> externalOrderList = new ArrayList<ExternalOrder>();
		for(Order order: orderList){
			externalOrderList.add(ConversionUtility.convert(order));
		}
		return externalOrderList;
	}
	
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
	
	public ExternalProduct getActiveProducts(int productId){
		return ConversionUtility.convert(productDao.getActiveProducts(productId));
	}
}
