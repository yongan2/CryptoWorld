package cw.feedhandler;

import cw.feedhandler.binance.BinanceWebSocketMarketDataHandler;

import java.util.ArrayList;
import java.util.List;

public class FeedHandlerServer {
    private final List<AbstractWebSocketMarketDataHandler> webSocketMarketDataHandlers;

    private FeedHandlerServer() throws Exception {
        this.webSocketMarketDataHandlers = new ArrayList<>();
        this.webSocketMarketDataHandlers.add(new BinanceWebSocketMarketDataHandler());
    }

    private void connect() {
        for (AbstractWebSocketMarketDataHandler webSocketMarketDataHandler : this.webSocketMarketDataHandlers) {
            webSocketMarketDataHandler.connect();
        }
    }

    public static void main(String[] args) throws Exception {
        new FeedHandlerServer().connect();
    }
}
