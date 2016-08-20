package com.warehouse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.object.external.ExternalOrder;
import com.warehouse.object.external.ExternalOrderStatus;
import com.warehouse.object.internal.OrderChangeRequest;
import com.warehouse.service.Service;

@RestController
public class OrderingController {
	
	@Autowired
	private Service service;

	@RequestMapping(value = "/create", method = RequestMethod.POST )
	public ExternalOrder createOrder() {
		return service.createOrder();
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST )
	public ExternalOrder submitOrder(int orderId){
		return service.submitOrder(orderId);	
	}
	
	@RequestMapping(value = "/change", method = RequestMethod.POST )
	public ExternalOrder changeOrder(int orderId, OrderChangeRequest orderChangeRequest){
		return service.changeOrder(orderId, orderChangeRequest);	
	}
	
	
	@RequestMapping(value = "/getorderbyid/{orderId}", method = RequestMethod.GET )
	public ExternalOrder getOrderById(@PathVariable("orderId") Integer orderId){
		return service.getOrderById(orderId);	
	}	
	
	@RequestMapping(value = "/getorderbystatus/{orderStatus}", method = RequestMethod.GET )
	public List<ExternalOrder> getOrderByStatus(@PathVariable("orderStatus") String orderStatus){
		return service.getOrderByStatus(convertStringToOrderStatus(orderStatus));	
	}	
	
	private ExternalOrderStatus convertStringToOrderStatus(String orderStatus) {
		Map<String, ExternalOrderStatus> orderStatusMap = new HashMap<String, ExternalOrderStatus>();
		orderStatusMap.put("open", ExternalOrderStatus.OPEN);
		orderStatusMap.put("received", ExternalOrderStatus.RECEIVED);
		orderStatusMap.put("processing", ExternalOrderStatus.PROCESSING);
		orderStatusMap.put("delivery", ExternalOrderStatus.DELIVERY);
		orderStatusMap.put("delivered", ExternalOrderStatus.DELIVERED);
		orderStatusMap.put("canceled", ExternalOrderStatus.CANCELLED);
		
		return orderStatusMap.get(orderStatus);
	}

	@RequestMapping(value = "/test")
	public String giveString() {
		return "I'm alive";
	}
	
	
}
