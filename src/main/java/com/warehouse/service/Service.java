package com.warehouse.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.warehouse.dao.OrderDao;
import com.warehouse.object.external.ExternalOrder;
import com.warehouse.object.external.ExternalOrderStatus;
import com.warehouse.object.external.ExternalProduct;
import com.warehouse.object.internal.Order;
import com.warehouse.object.internal.OrderChangeRequest;

@Component
public class Service {

	@Autowired
	private OrderDao orderDao;

	public ExternalOrder createOrder() {
		return convertInternalToExternal(orderDao.createOrder());
	}

	public Order changeOrder(String orderId, OrderChangeRequest orderChangeRequest) {
		return orderDao.changeOrder(orderId, orderChangeRequest);
	}
	
	private ExternalOrder convertInternalToExternal(Order order){
		ExternalOrder externalOrder = new ExternalOrder();
		externalOrder.setOrderId(order.getId());
		externalOrder.setOrderStatus(ExternalOrderStatus.convert(order.getOrderStatus()));
		externalOrder.setProducts(new HashMap<ExternalProduct, Integer>());
		return externalOrder;
	}

}
