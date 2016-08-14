package com.warehouse.object.internal;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "orders")
@Table(name = "orders")
public class Order {

	private String id;
	private OrderStatus orderStatus;

	private Set<OrderProduct> orderProduct = new HashSet<OrderProduct>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
	public Set<OrderProduct> getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(Set<OrderProduct> orderProduct) {
		this.orderProduct = orderProduct;
	}

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "order_id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

}
