package com.warehouse.dao;


import java.util.Set;

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

import com.warehouse.object.internal.Order;
import com.warehouse.object.internal.OrderChangeRequest;
import com.warehouse.object.internal.OrderCommand;
import com.warehouse.object.internal.OrderProduct;
import com.warehouse.object.internal.OrderStatus;
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
		
		assertEquals(OrderStatus.EMPTY, orderFirst.getOrderStatus());
		assertEquals(OrderStatus.EMPTY, orderSecond.getOrderStatus());
		
	}
	@Test
	public void testChangeOrder(){
		createTransaction();
		Product product = new Product("chicken", "meat", 4);
				
		Order order = new Order();
		order.setOrderStatus(OrderStatus.EMPTY);
		entityManager.persist(product);
		entityManager.persist(order);
		
		//Setting data for checking SET command. Checking that quantity changed to 3 
		OrderChangeRequest orderChangeRequestSet = new OrderChangeRequest(product.getId(), 3, OrderCommand.SET );
		
		entityManager.getTransaction().commit();
		orderDaoUnderTest.changeOrder(order.getId(), orderChangeRequestSet);
		entityManager.refresh(order);
		
		Set<OrderProduct> orderProductSet = order.getOrderProduct();
		OrderProduct orderProduct = orderProductSet.iterator().next();
		assertEquals(1,orderProductSet.size());
		assertEquals(3, orderProduct.getQuantity());
		assertEquals(OrderStatus.READY, order.getOrderStatus());
		
		assertEquals(orderProduct.getProduct().getId(), product.getId());
		assertEquals(orderProduct.getProduct().getName(), product.getName());
		assertEquals(orderProduct.getProduct().getDescription(), product.getDescription());
		assertEquals(orderProduct.getProduct().getPrice(), product.getPrice());
		assertEquals(orderProduct.getOrder().getId(), order.getId());
		
		//ADD command. Checking that quantity of products in oprderProduct increased by 2
		OrderChangeRequest orderChangeRequestAdd = new OrderChangeRequest(product.getId(), 2, OrderCommand.ADD);
		
		orderDaoUnderTest.changeOrder(order.getId(), orderChangeRequestAdd);
		entityManager.refresh(orderProduct);
			
		assertEquals(1,orderProductSet.size());
		assertEquals(5, orderProduct.getQuantity());
		assertEquals(OrderStatus.READY, order.getOrderStatus());
		
		//REMOVE command. Checking that quantity of products in oprderProduct decreased by 3
		OrderChangeRequest orderChangeRequestRemoveFirst = new OrderChangeRequest(product.getId(), 3, OrderCommand.REMOVE);
		
		orderDaoUnderTest.changeOrder(order.getId(), orderChangeRequestRemoveFirst);
		entityManager.refresh(orderProduct);

		assertEquals(1,orderProductSet.size());
		assertEquals(2, orderProduct.getQuantity());
		assertEquals(OrderStatus.READY, order.getOrderStatus());
		
		//REMOVE command. Checking that quantity of products in oprderProduct decreased to 0 in case of quantity value that higher than current 
		OrderChangeRequest orderChangeRequestRemoveSecond = new OrderChangeRequest(product.getId(), 3, OrderCommand.REMOVE);
		
		orderDaoUnderTest.changeOrder(order.getId(), orderChangeRequestRemoveSecond);
		entityManager.refresh(orderProduct);

		assertEquals(1,orderProductSet.size());
		assertEquals(0, orderProduct.getQuantity());
		assertEquals(OrderStatus.EMPTY, order.getOrderStatus());
	}
	
	@Test
	public void testSubmitOrder(){
		createTransaction();
		Order order = orderDaoUnderTest.createOrder();
		Product product = new Product("water", "drink", 5);	
			
		entityManager.persist(product);
		entityManager.getTransaction().commit();

		OrderChangeRequest orderChangeRequest = new OrderChangeRequest(product.getId(), 3, OrderCommand.SET);
		int orderId = order.getId();
		orderDaoUnderTest.changeOrder(orderId, orderChangeRequest);
		
		order = orderDaoUnderTest.submitOrder(orderId);
		
		assertEquals(OrderStatus.SUBMITTED, order.getOrderStatus());		
	}
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	@Test
	public void testSubmitOrderEmpty(){
	    exception.expect(RuntimeException.class);
	    exception.expectMessage("Wrong order status. Current status is - [EMPTY], expected - [READY]");
	    Order order = orderDaoUnderTest.createOrder();
	    orderDaoUnderTest.submitOrder(order.getId());
	}
	
	private EntityTransaction createTransaction() {
		EntityManagerFactory entityManagerFactory = transactionManager.getEntityManagerFactory();
		entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		return transaction;
	}

}
