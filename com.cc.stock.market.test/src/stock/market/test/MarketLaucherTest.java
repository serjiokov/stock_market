package stock.market.test;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.Assume;
import org.junit.Test;

import com.cc.stock.market.launcher.MarketStockSim;
import com.cc.stock.market.sim.Trade;
import com.cc.stock.market.sim.impl.Trades;

public class MarketLaucherTest {

	public static final String[] TEST_INPUT_S = { "S:U1:100:200", //
			"S:U2:100:200", //
			"S:U3:100:20", //
			"S:U4:100:2" };

	public static final String[] TEST_INPUT_B = { "B:U1:10:200", //
			"B:U2:20:200", //
			"B:U3:30:200", //
			"B:U4:40:200" };

	public static final String[] TEST_INCORECT_INPUT = { ":::", //
			"::", //
			":", //
			"", //
			"none", //
			"F:U4:100:200", //
			"S:U4:fff:200", //
			"S:U4:100:ff", //
			"S:U4:100:ff", //
			"B:U4:fff:30", //
			"B:U4:50:fff", //
			"1:1:1:1" };

	@Test
	public void shouldRunStopSuccessfully() {
		MarketStockSim simulator = new MarketStockSim();
		Thread sim = simulator.startMarketEngine();
		Assume.assumeNotNull(sim);
		Assume.assumeTrue(sim.isAlive());
		boolean isStopped = simulator.stopMarketEngine();
		assumeTrue(isStopped);
	}

	@Test
	public void shouldConnectGateAndMarketSuccessfully() {
		MarketStockSim simulator = new MarketStockSim();
		simulator.startMarketEngine();
		boolean isRegistered = simulator.register();
		Assume.assumeTrue(isRegistered);

		boolean isStopped = simulator.stopMarketEngine();
		assumeTrue(isStopped);
	}

	@Test
	public void shouldProcessIncorectInputsSuccessfully() {
		MarketStockSim simulator = new MarketStockSim();
		simulator.startMarketEngine();
		simulator.register();

		Stream.of(TEST_INCORECT_INPUT).forEach(c -> {
			String[] inputs = simulator.splitInput(c);
			boolean result = simulator.registerOrder(System.currentTimeMillis(), inputs);
			assumeFalse(result);
		});

		boolean isStopped = simulator.stopMarketEngine();
		assumeTrue(isStopped);
	}

	@Test
	public void shouldProcessInputMarketSuccessfully() {
		MarketStockSim simulator = new MarketStockSim();
		simulator.startMarketEngine();
		simulator.register();

		Stream.of(TEST_INPUT_S).forEach(c -> {
			String[] inputs = simulator.splitInput(c);
			simulator.registerOrder(System.currentTimeMillis(), inputs);

		});
		Stream.of(TEST_INPUT_B).forEach(c -> {
			String[] inputs = simulator.splitInput(c);
			simulator.registerOrder(System.currentTimeMillis(), inputs);
		});

		assumeTrue(simulator.getMarket().getBuyOffers().size() == 4);
		assumeTrue(simulator.getMarket().getSellOffers().size() == 4);
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			assumeNoException(e);
		}
		List<Trade> trades = Trades.getInstance().getTrades();
		Assume.assumeNotNull(trades);
		Assume.assumeFalse(trades.isEmpty());

		boolean isStopped = simulator.stopMarketEngine();
		assumeTrue(isStopped);
	}

}
