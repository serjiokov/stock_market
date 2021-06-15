/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.sim.impl;

import java.util.ArrayList;
import java.util.List;

import com.cc.stock.market.sim.Trade;

public class Trades {

	private List<Trade> trades = new ArrayList<>();

	public static Trades INSTANCE = new Trades();

	private Trades() {
		//
	}

	public static Trades getInstance() {
		return INSTANCE;
	}

	public List<Trade> getTrades() {
		return trades;
	}

	public void setTrade(Trade trade) {
		trades.add(trade);
	}

	public void setTrades(List<Trade> trades) {
		synchronized (this.trades) {
			this.trades.addAll(trades);
		}
	}

}
