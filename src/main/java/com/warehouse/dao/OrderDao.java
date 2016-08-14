package com.warehouse.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import com.warehouse.object.internal.Order;
import com.warehouse.object.internal.OrderChangeRequest;
import com.warehouse.object.internal.OrderCommand;
import com.warehouse.object.internal.OrderProduct;
import com.warehouse.object.internal.Product;

@Component
public class OrderDao {
	@Autowired
	private JpaTransactionManager transactionManager;
	/**
	 * Hibernate entity manager.
	 */
	private EntityManager entityManager;
	/**
	 * Creates an empty order.
	 * @return new order.
	 */
	public Order createOrder() {
		EntityTransaction transaction = createTransaction();
		Order order = new Order();
		entityManager.persist(order);
		transaction.commit();
		return order;
	}
	/**
	 * Gets the list of orders from OrderProduct table
	 * @param order from order table
	 * @param product from product table
	 * @return List that contains a unique pair of order and product from OrderProduct table
	 */	
	private List<OrderProduct> getOrderProductList(Order order, Product product){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderProduct> criteriaQuery = criteriaBuilder.createQuery(OrderProduct.class);
		Root<OrderProduct> root = criteriaQuery.from(OrderProduct.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("product"), product)),
				criteriaBuilder.equal(root.get("order"), order));
		
		List<OrderProduct> orderProducts = entityManager.createQuery(criteriaQuery).getResultList();
		return orderProducts;
	}
	
	/**
	 * Changes product's quantity in a specified order.
	 * @param orderId id of the order.
	 * @param orderChangeRequest contains command to change order.
	 * @return changed order.
	 */
	public Order changeOrder(String orderId, OrderChangeRequest orderChangeRequest) {
		createTransaction();
		String productId = orderChangeRequest.getProductId();
		Order order = entityManager.find(Order.class, orderId);//get an order from order table by id
		Product product = entityManager.find(Product.class, productId);//get a product from product table by id

		List<OrderProduct> orderProducts = getOrderProductList(order, product);
				
		OrderProduct orderProduct;
		if (orderProducts.isEmpty() && !orderChangeRequest.getOrderCommand().equals(OrderCommand.REMOVE)) {
			orderProduct = new OrderProduct(order, product, orderChangeRequest.getQuantity());
			
			entityManager.persist(orderProduct);
			entityManager.getTransaction().commit();
		} 
		else if(!orderProducts.isEmpty()) {
			orderProduct = orderProducts.get(0);
			OrderProduct changedOrderProduct = applyRequestCommand(orderProduct, orderChangeRequest);
			entityManager.persist(changedOrderProduct);
			entityManager.getTransaction().commit();
		}
		return order;
	}
	

	private OrderProduct applyRequestCommand(OrderProduct orderProduct,OrderChangeRequest orderChangeRequest ){
		switch (orderChangeRequest.getOrderCommand()){
			case REMOVE:
				int newQuantity = Math.max(0, orderProduct.getQuantity() - orderChangeRequest.getQuantity());
				orderProduct.setQuantity(newQuantity);
				break;
			case ADD:
				orderProduct.setQuantity(orderProduct.getQuantity() + orderChangeRequest.getQuantity());
				break;
			case SET:
				orderProduct.setQuantity(orderChangeRequest.getQuantity());
				break;
		}		
		return orderProduct;
	}
	

	private EntityTransaction createTransaction() {
		EntityManagerFactory entityManagerFactory = transactionManager.getEntityManagerFactory();
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.setFlushMode(FlushModeType.AUTO);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		return transaction;
	}

}
