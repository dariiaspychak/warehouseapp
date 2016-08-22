package com.warehouse.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.warehouse.object.internal.Product;

import junit.framework.TestCase;

@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductDaoTest extends TestCase {
	
	@Autowired
	private ProductDao productDaoUnderTest;
	@Autowired
	private JpaTransactionManager transactionManager;
	private EntityManager entityManager;

	protected void setUp() throws Exception {
		super.setUp();
		EntityManagerFactory entityManagerFactory = transactionManager.getEntityManagerFactory();
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.setFlushMode(FlushModeType.AUTO);
	}
	
	private EntityTransaction createTransaction() {
		EntityManagerFactory entityManagerFactory = transactionManager.getEntityManagerFactory();
		entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		return transaction;
	}
	
	@Test
	public void addNewProductTest(){
		Product product = productDaoUnderTest.addNewProduct("pork", "meat", 3, true);
		
		createTransaction();
		Product addedProduct = entityManager.find(Product.class, product.getProductId());
		entityManager.getTransaction().commit();
		
		assertNotNull(addedProduct);
		assertEquals("pork", addedProduct.getName());
		assertEquals("meat", addedProduct.getDescription());
		assertEquals(3, addedProduct.getPrice());
		assertTrue(addedProduct.isActive());
	} 
	
	@Test
	public void activateProductTest(){
		Product product = productDaoUnderTest.addNewProduct("pork", "meat", 3, false);
		productDaoUnderTest.activateProduct(product.getProductId());
		
		createTransaction();
		Product addedProduct = entityManager.find(Product.class, product.getProductId());
		entityManager.getTransaction().commit();
		assertTrue(addedProduct.isActive());
	}
	
	@Test
	public void deactivateProductTest(){
		Product product = productDaoUnderTest.addNewProduct("pork", "meat", 3, true);
		productDaoUnderTest.deactivateProduct(product.getProductId());
		
		createTransaction();
		Product addedProduct = entityManager.find(Product.class, product.getProductId());
		entityManager.getTransaction().commit();
		assertFalse(addedProduct.isActive());
	}
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	@Test
	public void getActiveProductByIdTest(){
	    exception.expect(RuntimeException.class);
	    exception.expectMessage("Product with ID [1] is not active.");

	    productDaoUnderTest.addNewProduct("pork", "meat", 3, false);
	    
	    productDaoUnderTest.getActiveProductById(1);
	}
}
