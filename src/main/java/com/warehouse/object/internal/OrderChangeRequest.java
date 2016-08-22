package com.warehouse.object.internal;


public class OrderChangeRequest {
	private int productId;
	private int quantity;
	private OrderCommand orderCommand;
	
	public OrderChangeRequest() {
	}

	public OrderChangeRequest(int productId, int quantity, OrderCommand orderCommand) {
		this.productId = productId;
		this.quantity = quantity;
		this.orderCommand = orderCommand;
	}
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
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
