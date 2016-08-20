package com.warehouse.object.internal;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "orders")
@Table(name = "orders")
public class Order {

	private int id;
	private OrderStatus orderStatus;

	private Set<OrderProduct> orderProduct = new HashSet<OrderProduct>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.MERGE)
	public Set<OrderProduct> getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(Set<OrderProduct> orderProduct) {
		this.orderProduct = orderProduct;
	}

	@Id
	@GeneratedValue
	@Column(name = "order_id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "order_status")
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

}
