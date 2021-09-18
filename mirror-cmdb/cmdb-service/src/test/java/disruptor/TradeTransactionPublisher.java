package disruptor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 模拟生产者，产生订单线程
 */
public class TradeTransactionPublisher implements Runnable {

    Disruptor<TradeTransaction> disruptor;
    private CountDownLatch latch;
    private static int LOOP = 10000;   //模拟一万次交易的发生

    public TradeTransactionPublisher(CountDownLatch latch,Disruptor<TradeTransaction> disruptor) {
        this.disruptor=disruptor;
        this.latch=latch;
    }

    @Override public void run() {
        TradeTransactionEventTranslator tradeTransloator = new TradeTransactionEventTranslator();
        for(int i=0;i<LOOP;i++){
            disruptor.publishEvent(tradeTransloator);
        }
        latch.countDown();
    }
}

class TradeTransactionEventTranslator implements EventTranslator<TradeTransaction> {

    private Random random = new Random();

    @Override public void translateTo(TradeTransaction event, long sequence) {
        this.generateTradeTransaction(event);
    }

    //生成订单流水号逻辑
    private TradeTransaction generateTradeTransaction(TradeTransaction trade) {
        trade.setId(random.nextInt() * 9999+"");
        return trade;
    }
}


