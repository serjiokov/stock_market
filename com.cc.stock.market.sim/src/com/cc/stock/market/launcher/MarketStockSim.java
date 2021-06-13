/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.launcher;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import com.cc.stock.market.sim.MarketEngine;
import com.cc.stock.market.sim.Trade;
import com.cc.stock.market.sim.TradingGate;
import com.cc.stock.market.sim.impl.MarketEngineImpl;
import com.cc.stock.market.sim.impl.TradingGateImpl;

public class MarketStockSim {
	private static final String version = "1.0.0"; //$NON-NLS-1$
	private static final String name = "Market Stock One"; //$NON-NLS-1$

	private static final int TRADE_DURATION = 2000;
	private static SimpleDateFormat dtFormat = new SimpleDateFormat("hh:mm:ss"); //$NON-NLS-1$
	private static MarketEngine mrktEngine;
	private static TradingGate trdGate;

	private static Logger logger;

	private static final String CMD_EXIT = "E"; //$NON-NLS-1$
	private static final String CMD_BUY_FORMAT = "B"; //$NON-NLS-1$
	private static final String CMD_SELL_FORMAT = "S"; //$NON-NLS-1$

	public static void main(String[] args) {
		logger = System.getLogger(MarketStockSim.class.getName());
		// introduction
		startPromt();

		// register engine and gates
		register();

		// start simulator
		startMarketEngine();

		// start reading
		startInteraction();

	}

	private static void startInteraction() {
		Scanner scanner = new Scanner(System.in);
		String line = "";
		long timestamp = -1L;
		System.out.println("The next format supproted: [B,S:name:count:price], [E - for exit]\n");
		while (true) {
			String dateTime = dtFormat.format(System.currentTimeMillis());
			System.out.printf(String.format("[%s]: ", dateTime));
			line = scanner.next();

			timestamp = System.currentTimeMillis();
			if (line == null || line.isEmpty()) {
				continue;
			}
			String[] inputs = line.split(":");

			// check on exit
			if (checkExit(inputs)) {
				System.out.printf("Simulator closed by user ...");
				scanner.close();
				System.exit(0);
			}
			// no exit matched
			if (!isValid(inputs)) {
				continue;
			}

			registerOrder(timestamp, inputs);
			mrktEngine.showMarketStatus();
		}
	}

	private static void register() {
		mrktEngine = getRegiseredMarketEngine();
		trdGate = getTradingGate();
		trdGate.connectToMarket(mrktEngine);
	}

	private static void startPromt() {
		System.out.println("<<< " + name + "[version: " + version + "] started >>>");
	}

	private static boolean isValid(String[] inputs) {
		if (inputs.length != 4) {
			logger.log(Level.ERROR, "Input format not valid [count of items]\n ");
			return false;
		}
		if (!inputs[0].equals(CMD_BUY_FORMAT) && !inputs[0].equals(CMD_SELL_FORMAT)) {
			logger.log(Level.ERROR, "Input format for [B,S] not valid ");
			return false;
		}
		if (inputs[1].isEmpty()) {
			logger.log(Level.ERROR, "Input format for [name] is empty or null\n");
			return false;
		}
		if (Integer.valueOf(inputs[2].trim()) == null) {
			logger.log(Level.ERROR, "Input format for [count] is not valid\n");
			return false;
		}
		if (Integer.valueOf(inputs[3].trim()) == null) {
			logger.log(Level.ERROR, "Input format for [price] is not valid\n");
			return false;
		}
		return true;
	}

	private static void registerOrder(long timestamp, String[] inputs) {
		if (CMD_BUY_FORMAT.equals(inputs[0])) {
			trdGate.registerBuyOrder(timestamp, inputs[1], Integer.valueOf(inputs[2].trim()),
					Integer.valueOf(inputs[3].trim()));
		}
		if (CMD_SELL_FORMAT.equals(inputs[0])) {
			trdGate.registerSellOrder(timestamp, inputs[1], Integer.valueOf(inputs[2].trim()),
					Integer.valueOf(inputs[3].trim()));
		}
	}

	private static void startMarketEngine() {
		(new Thread() {
			@Override
			public void run() {
				while (true) {
					List<Trade> trades = mrktEngine.trade();
					if (trades == null || trades.isEmpty()) {
						continue;
					}
					System.out.println("Trade(s) created:");
					trades.forEach(System.out::println);
					try {
						Thread.sleep(TRADE_DURATION);
					} catch (InterruptedException e) {
						logger.log(Level.ERROR, e.getMessage());
					}

					mrktEngine.showMarketStatus();
				}
			}
		}).start();
	}

	private static boolean checkExit(String[] inputs) {
		if (inputs != null && inputs.length > 0 && inputs[0].equals(CMD_EXIT)) {
			return true;
		}
		return false;
	}

	/**
	 * For cases based on another implementation
	 * 
	 * @return {@link MarketEngine}
	 */
	private static MarketEngine getRegiseredMarketEngine() {
		return new MarketEngineImpl();
	}

	/**
	 * For cases based on another implementation
	 * 
	 * @return {@link TradingGate}
	 */
	private static TradingGate getTradingGate() {
		return new TradingGateImpl();
	}

}
