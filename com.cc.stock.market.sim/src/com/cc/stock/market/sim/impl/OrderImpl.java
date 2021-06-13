/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.sim.impl;

import com.cc.stock.market.sim.Order;
import com.cc.stock.market.sim.OrderType;

public class OrderImpl implements Order {

	private OrderType type;
	private String name;
	private Long timestamp;
	private int quantity;
	private int price;

	public OrderImpl(OrderType type, String name, Long timestamp, int quantity, int price) {
		this.type = type;
		this.name = name;
		this.timestamp = timestamp;
		this.quantity = quantity;
		this.price = price;
	}

	@Override
	public Long getTimeStamp() {
		return this.timestamp;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getQuantity() {
		return this.quantity;
	}

	@Override
	public int getPrice() {
		return this.price;
	}

	@Override
	public OrderType getType() {
		return type;
	}

	@Override
	public int getStatus() {
		// for further implementation
		return 0;
	}
	
	@Override
	public String toString() {
		return "uid: " +getTimeStamp() + //
				"\ttype:" +getType()+//
				"\tname: " + getName()+//
				"\tcount: " +getQuantity()+//
				"\tprice: " +getPrice();
	}

}
