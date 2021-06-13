/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.sim;

public interface TradingGate {

	
	boolean registerSellOrder(long timestamp, String name, int counts, int price);

	boolean registerBuyOrder(long timestamp, String name, int counts, int price);

	void connectToMarket(MarketEngine m);
}
