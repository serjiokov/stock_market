/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.sim.impl;

import com.cc.stock.market.sim.MarketEngine;
import com.cc.stock.market.sim.Order;
import com.cc.stock.market.sim.OrderType;
import com.cc.stock.market.sim.TradingGate;

public class TradingGateImpl implements TradingGate {
	private MarketEngine market = null;

	@Override
	public void connectToMarket(MarketEngine m) {
		this.market = m;
	}

	@Override
	public boolean registerSellOrder(long timestamp, String name, int counts, int price) {
		if (!isMarketConnected()) {
			return false;
		}
		Order order = new OrderImpl(OrderType.Sell, name, timestamp, counts, price);
		market.addSellOrder(order);
		return true;
	}

	@Override
	public boolean registerBuyOrder(long timestamp, String name, int counts, int price) {
		if (!isMarketConnected()) {
			return false;
		}
		Order order = new OrderImpl(OrderType.Buy, name, timestamp, counts, price);
		market.addBuyOrder(order);
		return true;
	}

	private boolean isMarketConnected() {
		return market != null;
	}

}
