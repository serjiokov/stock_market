/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.sim.impl;

import com.cc.stock.market.sim.Order;
import com.cc.stock.market.sim.Trade;

public class TradeImpl implements Trade {
	private Order buy;
	private Order sell;
	private long timestamp;

	public TradeImpl(long timestamp, Order buy, Order sell) {
		super();
		this.buy = buy;
		this.sell = sell;
		this.timestamp = timestamp;
	}

	@Override
	public long getTradeUid() {
		// consider uid as timestamp
		return timestamp;
	}

	@Override
	public Order getOrderSell() {
		return sell;
	}

	@Override
	public Order getOrderBuy() {
		return buy;
	}

	@Override
	public String toString() {
		return " id:" + timestamp + //
				"\t name:" + buy.getName() + //
				"\t quantity(counts): " + buy.getQuantity() + //
				"\t price: " + buy.getPrice();
	}

}
