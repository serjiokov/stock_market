/**
 * Copyright Apache License 2.0
 */
package com.cc.stock.market.launcher;

public class Launcher {

	public static void main(String[] args) {
		MarketStockSim sim = new MarketStockSim();
		// introduction
		sim.startPromt();
		// register engine and gates
		sim.register();
		// start simulator
		sim.startMarketEngine();
		// start reading
		sim.startInteraction();
	}
}
