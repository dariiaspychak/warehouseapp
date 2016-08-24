package com.warehouse.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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
import com.warehouse.object.internal.OrderProduct;
import com.warehouse.object.internal.OrderStatus;
import com.warehouse.object.internal.Product;

@Component
public class OrderDao {
	
	@PostConstruct
	public void initEntityManager(){
		EntityManagerFactory entityManagerFactory = transactionManager.getEntityManagerFactory();
		if (entityManager == null) {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.setFlushMode(FlushModeType.AUTO);
		}
	}
	
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
		EntityTransaction transaction = getTransaction();
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
		Order order = entityManager.find(Order.class, orderId);
		Product product = entityManager.find(Product.class, orderChangeRequest.getProductId());

		applyRequestCommand(order, product, orderChangeRequest);
		applyStatus(order);
		return order;
	}

	private void applyStatus(Order order) {
		getTransaction();
		int overallQuantity = 0;
		for(OrderProduct orderProduct: order.getOrderProduct()){
			if(orderProduct.getQuantity() > 0){
				overallQuantity = orderProduct.getQuantity();
				break;
			}
		}
		if(overallQuantity == 0)
			order.setOrderStatus(OrderStatus.EMPTY);
		else
			order.setOrderStatus(OrderStatus.READY);
		entityManager.getTransaction().commit();
	}

	/**
	 * Applies request command, sets order status to order table
	 * 
	 * @param orderProduct
	 * @param orderChangeRequest
	 *            contains command to change order
	 */

	private void applyRequestCommand(Order order, Product product, OrderChangeRequest orderChangeRequest) {
		getTransaction();
		List<OrderProduct> orderProducts = getOrderProductList(order, product);
		int oldQuantity = getOldQuantity(orderProducts);
		int newQuantity = 0;
		switch (orderChangeRequest.getOrderCommand()) {
		case REMOVE:
			newQuantity = Math.max(0, oldQuantity - orderChangeRequest.getQuantity());
			break;
		case ADD:
			newQuantity = oldQuantity + orderChangeRequest.getQuantity();
			break;
		case SET:
			newQuantity = orderChangeRequest.getQuantity();
			break;
		}
		if(newQuantity == 0){
			removeOrderProduct(order, product, orderProducts);
		} else {
			applyNewQuantity(order, product, orderProducts, newQuantity);
		}
		entityManager.getTransaction().commit();
		entityManager.refresh(order);
		entityManager.refresh(product);
	}

	private void removeOrderProduct(Order order, Product product, List<OrderProduct> orderProducts) {
		for(OrderProduct orderProduct: orderProducts){
			entityManager.remove(orderProduct);
		}
	}


	private int getOldQuantity(List<OrderProduct> orderProducts) {
		return orderProducts.isEmpty() ? 0 : orderProducts.get(0).getQuantity();
	}

	private void applyNewQuantity(Order order, Product product, List<OrderProduct> orderProducts, int newQuantity) {
		if(orderProducts.isEmpty()){
			OrderProduct orderProduct = new OrderProduct();
			orderProduct.setOrder(order);
			orderProduct.setProduct(product);
			orderProduct.setQuantity(newQuantity);
			entityManager.persist(orderProduct);
		} else {
			OrderProduct orderProduct = orderProducts.get(0);
			orderProduct.setQuantity(newQuantity);
		}
	}

	private EntityTransaction getTransaction() {
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
		getTransaction();
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

	public Order getOrder(int orderId) {
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
	public List<Order> getOrders(OrderStatus[] orderStatus) {
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
