package cw.trader.strategy;

import cw.common.config.StrategyType;
import cw.common.db.mysql.StrategyConfig;
import cw.common.id.IdGenerator;
import cw.common.md.Exchange;
import cw.common.md.MarketDataType;
import cw.common.md.Quote;
import cw.common.md.TradingPair;
import cw.common.timer.ITimeManager;
import cw.common.timer.Timer;
import cw.trader.OrderResponse;
import cwp.db.IDbAdapter;
import net.openhft.chronicle.map.ChronicleMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractTraderStrategy {
    protected final ChronicleMap<TradingPair, Quote> chronicleMap;
    protected final Quote quote;
    protected final IDbAdapter dbAdapter;
    protected final ITimeManager timeManager;
    protected final Consumer<Timer> timerScheduler;
    protected final Exchange exchange;
    protected final TradingPair tradingPair;

    protected final int id;
    protected final Map<Long, String> orderIdToClientOrderId;

    public AbstractTraderStrategy(ChronicleMap<TradingPair, Quote> chronicleMap, Quote quote, IDbAdapter dbAdapter, ITimeManager timeManager, Consumer<Timer> timerScheduler, Exchange exchange, TradingPair tradingPair) {
        this.chronicleMap = chronicleMap;
        this.quote = quote;
        this.dbAdapter = dbAdapter;
        this.timeManager = timeManager;
        this.timerScheduler = timerScheduler;
        this.exchange = exchange;
        this.tradingPair = tradingPair;

        this.id = IdGenerator.nextId();
        this.orderIdToClientOrderId = new HashMap<>();
    }

    public Exchange getExchange() {
        return this.exchange;
    }

    public TradingPair getTradingPair() {
        return this.tradingPair;
    }

    public int getId() {
        return this.id;
    }

    public void updateOrderClientOrderId(long id, String clientOrderId) {
        this.orderIdToClientOrderId.put(id, clientOrderId);
    }

    public void scheduleTimer(Timer timer) {
        this.timerScheduler.accept(timer);
    }

    public abstract StrategyType getStrategyType();

    public abstract void onTimerEvent(Timer timer) throws Exception;

    public abstract void onStrategyConfig(StrategyConfig strategyConfig);

    public abstract void onOrderResponse(OrderResponse orderResponse);

    public abstract Collection<MarketDataType> getInterestedMarketDataTypes();
}
