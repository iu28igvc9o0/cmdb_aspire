/**
 * @Title: ESIndexer.java
 * @Package com.migu.tsg.microservice.logcontroller.es
 * @Description:  Copyright: Copyright (c) 2017 Company:咪咕文化 tsg
 * @author tsg-frank
 * @date 2017年7月27日 下午4:58:06
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.engine;

import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.Enviroment;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.dto.model.BusinessLogDTO;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.util.DateUtils;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.dto.LogInfoDTO;

/**
 * 日志解压，写入功能
 * description: Copyright: Copyright (c) 2017 Company: 咪咕文化 tsg
 * package: com.migu.tsg.microservice.monitor.log.es
 * title: ESIndexMaker.java
 * version: V1.0.0.0
 * author: sunke
 * date: 2017/9/11 22:41
 */
public class ESIndexMaker implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    private static final int ZIP_BUFFER_SIZE = 2048;

    private static final Gson GSON = new Gson();

    private static final JsonParser JSONPARSER = new JsonParser();

    // 解压后字符串转义字符替换字符
    private static final String NEW_CHAR = "'";

    // 解压后的字符串需要替换的转义字符串
    private static final String OLD_CHAR = "\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"";

    private final JestClient client;

    private final KafkaStream<byte[], byte[]> partition;

    private final int threadNum;

    private final String indexName;

    /**
     * build es indexer 创建一个新的实例 ESIndexMaker.
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * @param partition
     *            partition
     * @param threadNum
     *            threadNum
     * @param client
     *            client
     * @param indexName
     *            indexName
     */
    public ESIndexMaker(KafkaStream<byte[], byte[]> partition, int threadNum, JestClient client, String indexName) {
        this.partition = partition;
        this.threadNum = threadNum;
        this.client = client;
        this.indexName = indexName;
    }

    private byte[] unzipByteArray(byte[] file) throws IOException {
        byte[] byReturn = null;
        final Inflater oInflate = new Inflater();
        oInflate.setInput(file);
        final ByteArrayOutputStream oZipStream = new ByteArrayOutputStream();
        try {
            while (!oInflate.finished()) {
                byte[] byRead = new byte[ZIP_BUFFER_SIZE];
                int iBytesRead = oInflate.inflate(byRead);
                if (iBytesRead == byRead.length) {
                    oZipStream.write(byRead);
                } else {
                    oZipStream.write(byRead, 0, iBytesRead);
                }
            }
            byReturn = oZipStream.toByteArray();
        } catch (DataFormatException ex) {
            throw new IOException("Attempting to unzip file that is not zipped.", ex);
        } finally {
            oZipStream.close();
        }
        return byReturn;
    }

    /**
     *  简单描述该方法的实现功能（可选）.
     * @see java.lang.Runnable#run()
     */
    public void run() {
        LOG.info("consume kafka message threadnum = {}", threadNum);
        final ConsumerIterator<byte[], byte[]> it = partition.iterator();
        while (it.hasNext()) {
            try {
                final MessageAndMetadata<byte[], byte[]> item = it.next();
                final String jsonMessage = new String(unzipByteArray(item.message()), StandardCharsets.UTF_8);
                final String json = jsonMessage.replaceAll(OLD_CHAR, NEW_CHAR);
                final JsonArray jsonArray = JSONPARSER.parse(json).getAsJsonArray();
                final Iterator<JsonElement> jsonArrayIterator = jsonArray.iterator();
                final Bulk.Builder bulkBuilder = new Bulk.Builder();

                if (indexName.startsWith(Enviroment.BIZ_LOG_TYPE)) {
                    buildBizLogBulk(jsonArrayIterator, bulkBuilder);
                } else {
                    buildSystemLogBulk(jsonArrayIterator, bulkBuilder);
                }
                final BulkResult result = client.execute(bulkBuilder.build());
                if (!result.isSucceeded()) {
                    LOG.error("insert index data error");
                }
            } catch (Exception e) {
                LOG.error("ConsumerIterator iterator error", e);
            }
        }
    }

    private void buildBizLogBulk(Iterator<JsonElement> jsonArrayIterator, Bulk.Builder bulkBuilder) {
        BusinessLogDTO tempLogData;
        final Date now = new Date();
        while (jsonArrayIterator.hasNext()) {
            final JsonElement element = jsonArrayIterator.next();
            try {
                tempLogData = GSON.fromJson(element, BusinessLogDTO.class);
                tempLogData.setCreateDate(DateUtils.getDateStr(now, DateUtils.DATA_PATTERN_FULL_SYMBOL));
                final Index index = new Index.Builder(tempLogData).index(indexName)
                        .type(EsIndexManager.getBizType()).build();
                bulkBuilder.addAction(index);
            } catch (JsonSyntaxException e) {
                LOG.error("data [" + element.getAsString() + "] change json to BusinessLogDTO is error:", e);
            }
        }
    }

    private void buildSystemLogBulk(Iterator<JsonElement> jsonArrayIterator, Bulk.Builder bulkBuilder) {
        LogInfoDTO tempLogData;
        final Date now = new Date();
        while (jsonArrayIterator.hasNext()) {
            final JsonElement element = jsonArrayIterator.next();
            tempLogData = GSON.fromJson(element, LogInfoDTO.class);
            tempLogData.setTimestamp(DateUtils.getTimestamp(now));
            Index index = new Index.Builder(tempLogData).index(indexName).type(EsIndexManager.getLogType()).build();
            bulkBuilder.addAction(index);
        }
    }
}
