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

@Entity(name="products")
@Table(name="products")
public class Product {
	private int productId;
	private String name;
	private String description;
	private boolean isActive;
	
	private int price;
	
	private Set<OrderProduct> orderProduct = new HashSet<OrderProduct>(0);
	
	public Product() {
	}

	public Product(String name, String description, int price, boolean isActive) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.isActive = isActive;
	}
	
	public Product(int productId, String name, String description, int price, boolean isActive) {
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.isActive = isActive;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.MERGE)
	public Set<OrderProduct> getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(Set<OrderProduct> orderProduct) {
		this.orderProduct = orderProduct;
	}
	
	@Id
	@GeneratedValue
	@Column(name = "product_id")
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "price")
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}	
	@Column(name = "is_active")
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	

}
