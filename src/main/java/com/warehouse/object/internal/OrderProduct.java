package com.warehouse.object.internal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="order_product")
@Table(name="order_product")
public class OrderProduct {
	private String id;
	private Order  order;
	private Product product;
	private int quantity;
		
	public OrderProduct() {
	}

	public OrderProduct(Order order, Product product, int quantity) {
		this.order = order;
		this.product = product;
		this.quantity = quantity;
	}
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "order_id")
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "product_id")
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "product_order_id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
