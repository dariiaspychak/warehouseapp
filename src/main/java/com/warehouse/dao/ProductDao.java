package com.warehouse.dao;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import com.warehouse.object.internal.Product;


@Component
public class ProductDao {
	
	@PostConstruct
	public void initEntityManager(){
		EntityManagerFactory entityManagerFactory = transactionManager.getEntityManagerFactory();
		if (entityManager == null) {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.setFlushMode(FlushModeType.AUTO);
		}
	}
	
	private EntityTransaction getTransaction() {
		EntityTransaction transaction = entityManager.getTransaction();
		if (!transaction.isActive()) {
			transaction.begin();
		}
		return transaction;
	}
	
	@Autowired
	private JpaTransactionManager transactionManager;
	/**
	 * Hibernate entity manager.
	 */
	private EntityManager entityManager;
	
	public Product createProduct(String name, String description, int price, boolean isActive) {
		getTransaction();
		Product product = new Product(name, description, price, isActive);
		entityManager.persist(product);
		entityManager.getTransaction().commit();
		return product;
	}

	public Product getProduct(int productId) {
		return entityManager.find(Product.class, productId);
	}


	public void deactivateProduct(int productId) {
		getTransaction();
		Product product = getProduct(productId);
		if (product.isActive()){
			product.setActive(false);
			entityManager.merge(product);
		}
		entityManager.getTransaction().commit();
	}
	
	public void activateProduct(int productId) {
		getTransaction();
		Product product = getProduct(productId);
		if (!product.isActive()){
			product.setActive(true);
			entityManager.merge(product);
		}
		entityManager.getTransaction().commit();
	}

	public Product getActiveProducts(int productId) {
		Product product = getProduct(productId);
		if (product.isActive()){
			return product;
		}
		else{
			throw new RuntimeException("Product with ID [" + productId + "] is not active.");
		}
	}
	
	
}
