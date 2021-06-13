/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.sim;

import java.util.List;

public interface MarketEngine {

	/**
	 * The method do a deal at some defined period
	 */
	Trade setDeal(Order buy, Order sell);

	/**
	 * The method checks sales and purchase by timestamp if order details matched
	 * (name) return pair.
	 */
	List<Trade> trade();

	/**
	 * Add order in sell list
	 * 
	 * @param offer - sell offer
	 */
	void addSellOrder(Order offer);

	/**
	 * Add order in buy list
	 * 
	 * @param offer - buy offer
	 */
	void addBuyOrder(Order offer);

	void showMarketStatus();

}
