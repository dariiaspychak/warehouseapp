package com.warehouse.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import com.warehouse.object.internal.Order;
import com.warehouse.object.internal.OrderChangeRequest;
import com.warehouse.object.internal.OrderCommand;
import com.warehouse.object.internal.OrderProduct;
import com.warehouse.object.internal.OrderStatus;
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
	 * 
	 * @return new order.
	 */
	public Order createOrder() {
		EntityTransaction transaction = createTransaction();
		Order order = new Order();
		order.setOrderStatus(OrderStatus.EMPTY);
		entityManager.persist(order);
		transaction.commit();
		return order;
	}

	/**
	 * Gets the list of orders from OrderProduct table
	 * 
	 * @param order
	 *            from order table
	 * @param product
	 *            from product table
	 * @return List that contains a unique pair of order and product from
	 *         OrderProduct table
	 */
	private List<OrderProduct> getOrderProductList(Order order, Product product) {
		createTransaction();
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
	 * 
	 * @param orderId
	 *            id of the order.
	 * @param orderChangeRequest
	 *            contains command to change order.
	 * @return changed order.
	 */
	public Order changeOrder(int orderId, OrderChangeRequest orderChangeRequest) {
		createTransaction();
		String productId = orderChangeRequest.getProductId();
		Order order = entityManager.find(Order.class, orderId);
		Product product = entityManager.find(Product.class, productId);
		List<OrderProduct> orderProducts = getOrderProductList(order, product);

		OrderProduct orderProduct;
		if (orderProducts.isEmpty() && !orderChangeRequest.getOrderCommand().equals(OrderCommand.REMOVE)) {
			orderProduct = new OrderProduct(order, product, orderChangeRequest.getQuantity());

			if (order.getOrderStatus().equals(OrderStatus.EMPTY) && orderChangeRequest.getQuantity() > 0) {
				order.setOrderStatus(OrderStatus.READY);
				entityManager.merge(order);
			}
			entityManager.persist(orderProduct);
			entityManager.getTransaction().commit();
		} else if (!orderProducts.isEmpty()) {
			orderProduct = orderProducts.get(0);
			OrderProduct changedOrderProduct = applyRequestCommand(orderProduct, orderChangeRequest);
			entityManager.getTransaction().commit();
		}
		entityManager.refresh(order);
		return order;
	}

	/**
	 * Applies request command, sets order status to order table
	 * 
	 * @param orderProduct
	 * @param orderChangeRequest
	 *            contains command to change order
	 */

	private OrderProduct applyRequestCommand(OrderProduct orderProduct, OrderChangeRequest orderChangeRequest) {
		switch (orderChangeRequest.getOrderCommand()) {
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
		if (entityManager == null) {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.setFlushMode(FlushModeType.AUTO);
		}
		EntityTransaction transaction = entityManager.getTransaction();
		if (!transaction.isActive()) {
			transaction.begin();
		}
		return transaction;
	}

	/**
	 * Sets status SUBMITTED for an order with a specified orderId
	 *
	 * @param orderId
	 *            Id of an order that needs to be submitted
	 * @return returns a submitted order
	 */
	public Order submitOrder(int orderId) {
		createTransaction();
		Order order = entityManager.find(Order.class, orderId);

		if (order.getOrderStatus().equals(OrderStatus.READY)) {
			order.setOrderStatus(OrderStatus.SUBMITTED);
		} else {
			throw new RuntimeException("Wrong order status. Current status is - [" + order.getOrderStatus()
					+ "], expected - [" + OrderStatus.READY + "]");
		}
		entityManager.persist(order);
		entityManager.getTransaction().commit();
		entityManager.refresh(order);
		return order;
	}

	public Order getOrderById(int orderId) {
		Order order = entityManager.find(Order.class, orderId);
		return order;
	}

	/**
	 * Finds a list of orders with a status from OrderStatus array
	 *
	 * @param orderStatus
	 *            array with order status
	 * @return returns a list of orders
	 */
	public List<Order> getOrderByStatus(OrderStatus[] orderStatus) {
		createTransaction();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		List<Predicate> restrictions = new ArrayList<Predicate>();
		for (OrderStatus orderStat : orderStatus) {
			restrictions.add(criteriaBuilder.equal(root.get("orderStatus"), orderStat));
		}
		Predicate[] predicateArray = new Predicate[restrictions.size()];
		criteriaQuery.where(criteriaBuilder.or(restrictions.toArray(predicateArray)));

		List<Order> orders = entityManager.createQuery(criteriaQuery).getResultList();
		return orders;
	}

}
