package com.warehouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

	@RequestMapping(value = "/order/new", method = RequestMethod.POST )
	public ExternalOrder createOrder() {
		return service.createOrder();
	}
	
	@RequestMapping(value = "/submit/{orderId}", method = RequestMethod.POST )
	public ExternalOrder submitOrder(@PathVariable("orderId") int orderId){
		return service.submitOrder(orderId);	
	}
	
	@RequestMapping(value = "/change/{orderId}", method = RequestMethod.POST )
	public ExternalOrder changeOrder(@PathVariable("orderId") int orderId, @RequestBody OrderChangeRequest orderChangeRequest){
		return service.changeOrder(orderId, orderChangeRequest);	
	}
	
	
	@RequestMapping(value = "/order/id/{orderId}", method = RequestMethod.GET )
	public ExternalOrder getOrder(@PathVariable("orderId") Integer orderId){
		return service.getOrder(orderId);	
	}	
	
	@RequestMapping(value = "/order/status/{orderStatus}", method = RequestMethod.GET )
	public List<ExternalOrder> getOrders(@PathVariable("orderStatus") String orderStatus){
		return service.getOrders(convertStringToOrderStatus(orderStatus));	
	}	
	
	private ExternalOrderStatus convertStringToOrderStatus(String orderStatus) {
		return ExternalOrderStatus.valueOf(orderStatus.toUpperCase());
	}

	@RequestMapping(value = "/test")
	public String giveString() {
		return "I'm alive";
	}
	
	
}
