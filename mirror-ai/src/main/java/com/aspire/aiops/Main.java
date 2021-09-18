package com.aspire.aiops;

import com.aspire.aiops.flink.KafkaFlinkConsumer;

/**
 * @author ：Vincent Hu
 * @date ：Created in 6/6/2019 09:59
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

public class Main {

    public static void main(String[] args) throws Exception{
        KafkaFlinkConsumer.run();
    }

}
