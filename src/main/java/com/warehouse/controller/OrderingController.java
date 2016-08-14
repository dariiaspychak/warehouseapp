package com.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.object.external.ExternalOrder;
import com.warehouse.service.Service;

@RestController
public class OrderingController {
	
	@Autowired
	private Service service;

	@RequestMapping(value = "/create")
	public ExternalOrder createOrder() {
		return service.createOrder();
	}
	
	@RequestMapping(value = "/test")
	public String giveString() {
		return "I'm alive";
	}
}
