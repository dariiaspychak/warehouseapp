package com.warehouse.dao;


import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.warehouse.object.internal.Order;
import com.warehouse.object.internal.OrderChangeRequest;
import com.warehouse.object.internal.OrderCommand;
import com.warehouse.object.internal.OrderProduct;
import com.warehouse.object.internal.Product;

import junit.framework.TestCase;

@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderDaoTest extends TestCase {
	
	@Autowired
	private OrderDao orderDaoUnderTest;
	@Autowired
	private JpaTransactionManager transactionManager;
	private EntityManager entityManager;
	
	protected void setUp() throws Exception {
		super.setUp();
		EntityManagerFactory entityManagerFactory = transactionManager.getEntityManagerFactory();
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.setFlushMode(FlushModeType.AUTO);
	}
	@Test
	public void testCreateOrder(){
		
		Order orderFirst = orderDaoUnderTest.createOrder();
		Order orderSecond = orderDaoUnderTest.createOrder();
		
		assertNotNull(orderFirst);
		assertNotNull(orderSecond);
		System.out.println(orderFirst.getId()+ " vs " +orderSecond.getId());
		assertNotSame(orderFirst.getId(), orderSecond.getId());
		
		
	}
	@Test
	public void testChangeOrder(){
		createTransaction();
		Product product = new Product("chicken", "meat", 4);
				
		Order order = new Order(); 
		entityManager.persist(product);
		entityManager.persist(order);
		
		OrderChangeRequest orderChangeRequestSet = new OrderChangeRequest();
		orderChangeRequestSet.setProductId(product.getId());
		orderChangeRequestSet.setQuantity(3);
		orderChangeRequestSet.setOrderCommand(OrderCommand.SET);
		
		entityManager.getTransaction().commit();
				
		orderDaoUnderTest.changeOrder(order.getId(), orderChangeRequestSet);
		entityManager.refresh(order);
		
		Set<OrderProduct> orderProductSet = order.getOrderProduct();
		OrderProduct orderProduct = orderProductSet.iterator().next();
		assertEquals(1,orderProductSet.size());
		assertEquals(3, orderProduct.getQuantity());
		
		assertEquals(orderProduct.getProduct().getId(), product.getId());
		assertEquals(orderProduct.getProduct().getName(), product.getName());
		assertEquals(orderProduct.getProduct().getDescription(), product.getDescription());
		assertEquals(orderProduct.getProduct().getPrice(), product.getPrice());
		
		assertEquals(orderProduct.getOrder().getId(), order.getId());
		
		OrderChangeRequest orderChangeRequestAdd = new OrderChangeRequest();
		orderChangeRequestAdd.setProductId(product.getId());
		orderChangeRequestAdd.setQuantity(2);
		orderChangeRequestAdd.setOrderCommand(OrderCommand.ADD);
		
		orderDaoUnderTest.changeOrder(order.getId(), orderChangeRequestAdd);
		entityManager.persist(orderProduct);
		entityManager.refresh(orderProduct);
			
		assertEquals(1,orderProductSet.size());
		assertEquals(5, orderProduct.getQuantity());
		
		OrderChangeRequest orderChangeRequestRemove = new OrderChangeRequest();
		orderChangeRequestRemove.setProductId(product.getId());
		orderChangeRequestRemove.setQuantity(3);
		orderChangeRequestRemove.setOrderCommand(OrderCommand.REMOVE);
		
		orderDaoUnderTest.changeOrder(order.getId(), orderChangeRequestRemove);
		entityManager.persist(orderProduct);
		entityManager.refresh(orderProduct);

		assertEquals(1,orderProductSet.size());
		assertEquals(2, orderProduct.getQuantity());
	}
	
	private EntityTransaction createTransaction() {
		EntityManagerFactory entityManagerFactory = transactionManager.getEntityManagerFactory();
		entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		return transaction;
	}

}
