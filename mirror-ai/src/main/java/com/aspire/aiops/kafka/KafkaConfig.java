package com.aspire.aiops.kafka;

import com.aspire.aiops.utils.ResourceUtil;

import java.util.Properties;

/**
 * @author ：Vincent Hu
 * @date ：Created in 6/11/2019 14:51
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
public class KafkaConfig {

    public static Properties getKafkaProperties(String kafkaConfigFile){
        //ResourceUtil.loadResource(kafkaConfigFile);
        return ResourceUtil.loadResource(kafkaConfigFile);
    }
}
