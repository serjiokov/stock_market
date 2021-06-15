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

	private final int TRADE_DURATION = 2000;
	private SimpleDateFormat dtFormat = new SimpleDateFormat("hh:mm:ss"); //$NON-NLS-1$
	private MarketEngine mrktEngine;
	private Thread mrktEngineThread;

	private TradingGate trdGate;

	private Logger logger = System.getLogger(MarketStockSim.class.getName());
	private static boolean isSimRunning = true;

	private static final String CMD_EXIT = "E"; //$NON-NLS-1$
	private static final String CMD_BUY_FORMAT = "B"; //$NON-NLS-1$
	private static final String CMD_SELL_FORMAT = "S"; //$NON-NLS-1$

	public void main(String[] args) {

		// introduction
		startPromt();

		// register engine and gates
		register();

		// start simulator
		startMarketEngine();

		// start reading
		startInteraction();

	}

	public void startInteraction() {
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
			// split
			String[] inputs = splitInput(line);

			// check on exit
			if (checkInputOnExit(inputs)) {
				System.out.printf("Simulator closed by the user request...");
				scanner.close();
				System.exit(0);
			}

			registerOrder(timestamp, inputs);
			mrktEngine.showMarketStatus();
		}
	}

	public String[] splitInput(String line) {
		return line.split(":");
	}

	public boolean register() {
		mrktEngine = getRegiseredMarketEngine();
		trdGate = getTradingGate();
		return trdGate.connectToMarket(mrktEngine);
	}

	public static void startPromt() {
		System.out.println("<<< " + name + "[version: " + version + "] started >>>");
	}

	public boolean isValidInputFormat(String[] inputs) {
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
		try {
			Integer.valueOf(inputs[2].trim());
		} catch (Exception e) {
			logger.log(Level.ERROR, "Input format for [count] is not valid\n");
			logger.log(Level.ERROR, e.getMessage());
			return false;
		}
		try {
			Integer.valueOf(inputs[3].trim());
		} catch (Exception e) {
			logger.log(Level.ERROR, "Input format for [price] is not valid\n");
			logger.log(Level.ERROR, e.getMessage());
			return false;
		}
		return true;
	}

	public boolean registerOrder(long timestamp, String[] inputs) {
		boolean result = false;
		// if no exit matched before
		if (!isValidInputFormat(inputs)) {
			return result;
		}
		if (CMD_BUY_FORMAT.equals(inputs[0])) {
			result = trdGate.registerBuyOrder(timestamp, inputs[1], Integer.valueOf(inputs[2].trim()),
					Integer.valueOf(inputs[3].trim()));
		}
		if (CMD_SELL_FORMAT.equals(inputs[0])) {
			result = trdGate.registerSellOrder(timestamp, inputs[1], Integer.valueOf(inputs[2].trim()),
					Integer.valueOf(inputs[3].trim()));
		}
		return result;

	}

	public Thread startMarketEngine() {
		isSimRunning = true;
		mrktEngineThread = new Thread() {
			@Override
			public void run() {
				while (isSimRunning) {
					if (mrktEngine == null) {
						// not yet connected waiting (...)
						continue;
					}
					List<Trade> trades = mrktEngine.trade();
					if (!trades.isEmpty()) {
						System.out.println("Trade(s) created for:");
						trades.forEach(System.out::println);
					}
					try {
						Thread.sleep(TRADE_DURATION);
					} catch (InterruptedException e) {
						logger.log(Level.ERROR, e.getMessage());
					}
					mrktEngine.showMarketStatus();
				}

			}
		};
		mrktEngineThread.start();
		return mrktEngineThread;
	}

	private static boolean checkInputOnExit(String[] inputs) {
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

	public boolean stopMarketEngine() {
		isSimRunning = false;
		mrktEngineThread.interrupt();
		return mrktEngineThread.isInterrupted();
	}

	public MarketEngine getMarket() {
		return mrktEngine;
	}

}
