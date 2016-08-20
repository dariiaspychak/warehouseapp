package com.warehouse.object.external;

import com.warehouse.object.internal.OrderStatus;

public enum ExternalOrderStatus {
	OPEN, RECEIVED, PROCESSING, DELIVERY, DELIVERED, CANCELLED;

	public static ExternalOrderStatus convert(OrderStatus orderStatus) {
		switch (orderStatus) {
		case EMPTY:
			return OPEN;
		case READY:
			return OPEN;
		case SUBMITTED:
			return RECEIVED;
		case PROCESSING:
			return PROCESSING;
		case READY_FOR_DELIVERY:
			return PROCESSING;
		case DELIVERY_IN_PROGRESS:
			return DELIVERY;
		case DELIVERED:
			return DELIVERED;
		case REJECTED:
			return CANCELLED;
		case CANCELLED:
			return CANCELLED;
		default:
			throw new RuntimeException("Unknown Order status: " + orderStatus.name());
		}
	}
}
