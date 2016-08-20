package com.warehouse.object.internal;

import com.warehouse.object.external.ExternalOrderStatus;

public enum OrderStatus {
	EMPTY,
	READY, //ready to submit
	SUBMITTED,
	PROCESSING,
	READY_FOR_DELIVERY,
	DELIVERY_IN_PROGRESS,
	DELIVERED,
	CANCELLED,
	REJECTED;
	
	public static OrderStatus[] convert(ExternalOrderStatus orderStatus) {
		switch (orderStatus) {
		case OPEN:
			return new OrderStatus[]{OrderStatus.EMPTY, OrderStatus.READY};
		case RECEIVED:
			return  new OrderStatus[]{SUBMITTED};
		case PROCESSING:
			return  new OrderStatus[]{PROCESSING, READY_FOR_DELIVERY};
		case DELIVERY:
			return  new OrderStatus[]{DELIVERY_IN_PROGRESS};
		case DELIVERED:
			return  new OrderStatus[]{DELIVERED};
		case CANCELLED:
			return  new OrderStatus[]{CANCELLED,REJECTED};
		default:
			throw new RuntimeException("Unknown Order status: " + orderStatus.name());
		}
	}
	
}
