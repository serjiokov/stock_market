/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.sim.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import com.cc.stock.market.sim.MarketEngine;
import com.cc.stock.market.sim.Order;
import com.cc.stock.market.sim.Trade;

public class MarketEngineImpl implements MarketEngine {
	private TreeMap<String, Order> saleOffers = new TreeMap<>();
	private TreeMap<String, Order> buyOffers = new TreeMap<>();

	@Override
	public Trade setDeal(Order buy, Order sell) {

		// should do latest check
		buyOffers.remove(buy.getName());
		saleOffers.remove(sell.getName());
		return new TradeImpl(System.currentTimeMillis(), buy, sell);

	}

	public void registerTrades(List<Trade> trades) {
		Trades.getInstance().setTrades(trades);
	}

	/**
	 * The method checks sales and purchase by timestamp if order details matched
	 * (name) return pair.
	 */
	@Override
	public List<Trade> trade() {
		List<Order> sallers = new ArrayList<>();
		List<Order> bayers = new ArrayList<>();
		buyOffers.entrySet().stream() //
				.forEach(b -> {
					Order saler = saleOffers.get(b.getKey());
					if (saler != null) {
						sallers.add(saler);
						bayers.add(b.getValue());
					}
				});

		sallers.sort(new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				if (o1.getTimeStamp() < o2.getTimeStamp()) {
					return 1;
				}
				return -1;
			}
		});

		bayers.sort(new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				if (o1.getTimeStamp() < o2.getTimeStamp()) {
					return 1;
				}
				return -1;
			}
		});

		List<Trade> trades = new ArrayList<>();
		getMatchesBtwBuyAndSell(bayers, sallers, (Order b, Order s) -> {
			Trade trade = setDeal(b, s);
			if (trade != null) {
				trades.add(trade);
			}
		});

		registerTrades(trades);
		return trades;
	}

	private static <T1, T2> void getMatchesBtwBuyAndSell(Iterable<T1> c1, Iterable<T2> c2,
			BiConsumer<T1, T2> consumer) {
		Iterator<T1> i1 = c1.iterator();
		Iterator<T2> i2 = c2.iterator();
		while (i1.hasNext() && i2.hasNext()) {
			consumer.accept(i1.next(), i2.next());
		}
	}

	@Override
	public void addSellOrder(Order offer) {
		saleOffers.put(offer.getName(), offer);
	}

	@Override
	public void addBuyOrder(Order offer) {
		buyOffers.put(offer.getName(), offer);
	}

	@Override
	public void showMarketStatus() {
		System.out.println("Status orders on market:");
		saleOffers.values().forEach(System.out::println);
		buyOffers.values().forEach(System.out::println);
	}

	@Override
	public TreeMap<String, Order> getBuyOffers() {
		return buyOffers;
	}

	@Override
	public TreeMap<String, Order> getSellOffers() {
		return saleOffers;
	}
}
