package com.warehouse.dao;

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
	@Autowired
	private JpaTransactionManager transactionManager;
	/**
	 * Hibernate entity manager.
	 */
	private EntityManager entityManager;
	

	private EntityTransaction createTransaction() {
		EntityManagerFactory entityManagerFactory = transactionManager.getEntityManagerFactory();
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.setFlushMode(FlushModeType.AUTO);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		return transaction;
	}


	public Product addNewProduct(String name, String description, int price, boolean isActive) {
		createTransaction();
		Product product = new Product(name, description, price, isActive);
		entityManager.persist(product);
		entityManager.getTransaction().commit();
		return product;
	}

	public Product getProductById(int productId) {
		return entityManager.find(Product.class, productId);
	}


	public void deactivateProduct(int productId) {
		createTransaction();
		Product product = getProductById(productId);
		if (product.isActive()){
			product.setActive(false);
			entityManager.merge(product);
		}
		entityManager.getTransaction().commit();
	}
	
	public void activateProduct(int productId) {
		createTransaction();
		Product product = getProductById(productId);
		if (!product.isActive()){
			product.setActive(true);
			entityManager.merge(product);
		}
		entityManager.getTransaction().commit();
	}

	public Product getActiveProductById(int productId) {
		Product product = getProductById(productId);
		if (product.isActive()){
			return product;
		}
		else{
			throw new RuntimeException("Product with ID [" + productId + "] is not active.");
		}
	}
	
	
}
