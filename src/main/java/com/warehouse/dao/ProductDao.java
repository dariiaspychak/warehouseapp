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


	public Product addNewProduct(String name, String description, int price) {
		createTransaction();
		Product product = new Product(name, description, price);
		entityManager.persist(product);
		entityManager.getTransaction().commit();
		return product;
	}
	
	public void removeProduct(String productId){
		createTransaction();
		Product product = getProductById(productId);
		entityManager.remove(product);
		entityManager.getTransaction().commit();
	}

	private Product getProductById(String productId) {
		return entityManager.find(Product.class, productId);
	}
}
