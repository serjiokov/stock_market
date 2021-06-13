/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.sim;

public interface Order {
 
	Long getTimeStamp();
	
	String getName();

	int getQuantity();

	int getPrice();

	OrderType getType();

	int getStatus();
}
