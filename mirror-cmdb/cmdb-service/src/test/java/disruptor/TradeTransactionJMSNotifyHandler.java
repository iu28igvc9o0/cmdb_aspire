package disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * 处理订单发送短信业务
 */
public class TradeTransactionJMSNotifyHandler implements EventHandler<TradeTransaction> {

    @Override public void onEvent(TradeTransaction tradeTransaction, long l, boolean b) throws Exception {
        String threadName = Thread.currentThread().getName();
        String id = tradeTransaction.getId();
        System.out.println("线程:" + threadName + "处理订单业务发送短信通知服务:  商品ID是 ：" + id);
    }
}

