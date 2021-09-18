package disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * 处理增值业务的线程
 */
public class TradeTransactionVasHandler implements EventHandler<TradeTransaction> {

    @Override public void onEvent(TradeTransaction tradeTransaction, long l, boolean b) throws Exception {
        String threadName = Thread.currentThread().getName();
        String id = tradeTransaction.getId();
        System.out.println("线程:" + threadName + "处理订单增值服务: 处理的 商品ID是 ：" + id);

    }
}

