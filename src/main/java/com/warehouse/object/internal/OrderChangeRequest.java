package com.warehouse.object.internal;


public class OrderChangeRequest {
	private String productId;
	private int quantity;
	
	private OrderCommand orderCommand;
	
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public OrderCommand getOrderCommand() {
		return orderCommand;
	}
	public void setOrderCommand(OrderCommand orderCommand) {
		this.orderCommand = orderCommand;
	}

}
