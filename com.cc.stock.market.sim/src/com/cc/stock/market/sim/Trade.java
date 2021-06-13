/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.sim;

public interface Trade {

	long getTradeUid();

	Order getOrderSell();

	Order getOrderBuy();
}
