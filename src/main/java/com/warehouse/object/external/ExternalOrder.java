package com.warehouse.object.external;

import java.util.Map;

public class ExternalOrder {
	
	private Map<ExternalProduct, Integer> products;
	private int orderId;
	private ExternalOrderStatus orderStatus;
	public Map<ExternalProduct, Integer> getProducts() {
		return products;
	}
	public void setProducts(Map<ExternalProduct, Integer> products) {
		this.products = products;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public ExternalOrderStatus getOrderStatus() {
		return orderStatus;
	}	
	public void setOrderStatus(ExternalOrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
}
