/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.sim.impl;

import java.util.ArrayList;
import java.util.List;

import com.cc.stock.market.sim.Trade;

public class Trades {

	private static List<Trade> trades = new ArrayList<>();

	public static List<Trade> getTrades() {
		return trades;
	}

	public static void setTrade(Trade trade) {
		trades.add(trade);
	}

	public static void setTrades(List<Trade> trades) {
		trades.addAll(trades);
	}

}
