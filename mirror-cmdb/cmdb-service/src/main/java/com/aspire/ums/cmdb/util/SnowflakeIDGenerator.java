package com.aspire.ums.cmdb.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分布式ID生成器.
 *
 * @author jiangxuwen
 * @date 2019/12/25 10:11
 */
public class SnowflakeIDGenerator {

    private static Logger log = LoggerFactory.getLogger(SnowflakeIDGenerator.class);

    /**
     * 标尺时间 2018-10-01 12:00:00 时间戳在64bits总所占位数: 41bits 最大时间戳的最大范围[0, 2199023255551] 从标尺时间开始，2199023255551毫秒(69.73057年)之后此ID生成器将失效
     */
    private final long twepoch = 1538366400000L;

    /**
     * 数据中心在64bits中所占的位数: 10bits
     */
    private final long dataCenterIdBits = 10L;

    /**
     * 序列在64bits中所占的位数: 12bits
     */
    private final long sequenceBits = 12L;

    /**
     * 数据中心最大的范围 [0, 1023]
     */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * 数据中心左移偏移量: 12bits
     */
    private final long dataCenterIdShift = sequenceBits;

    /**
     * 时间戳左移偏移量：12+10=22bits
     */
    private final long timestampLeftShift = sequenceBits + dataCenterIdBits;

    /**
     * 序列mask 00000000 00000000 00000000 0000000 00000000 00000000 00001111 11111111
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 数据中心ID
     */
    private long dataCenterId;

    /**
     * 原始算法默认从0开始, 改进方法：初始化时，随机取[0,1]其中一个 毫秒内累计的规则: 从0开始累积: 0,1,2,3,4...4095 从1开始累积: 1,2,3,4,5...4095 此字段涉及多线程并发写场景
     * 设置volatile保障happens-before 让写立刻对其他线程可见
     */
    private volatile long sequence = ThreadLocalRandom.current().nextInt(2);

    /**
     * 上次生成ID的时间截 此字段涉及多线程并发写场景 设置volatile保障happens-before 让写立刻对其他线程可见
     */
    private volatile long lastTimestamp = -1L;

    /**
     * 最大容忍时间, 单位毫秒, 即如果时钟只是回拨了该变量指定的时间, 那么等待相应的时间即可; 考虑到sequence服务的高性能, 这个值不易过大
     */
    private static final long MAX_BACKWARD_MS = 3;

    private Object lock = new Object();

    SecureRandom random = new SecureRandom();

    /**
     * @param dataCenterId
     *            数据中心ID范围 [0, 1023]
     */
    public SnowflakeIDGenerator(long dataCenterId) {
        if (dataCenterId == 0) {
            try {
                this.dataCenterId = getDataCenterId();
            } catch (SocketException | UnknownHostException | NullPointerException e) {
                this.dataCenterId = ThreadLocalRandom.current().nextInt((int) maxDataCenterId) + 1;
                log.warn("SNOWFLAKE: could not determine machine address; using random dataCenterId:{}", this.dataCenterId);
            }
        } else {
            this.dataCenterId = dataCenterId;
        }
        if (this.dataCenterId > maxDataCenterId || dataCenterId < 0) {
            this.dataCenterId = ThreadLocalRandom.current().nextInt((int) maxDataCenterId) + 1;
            log.warn("SNOWFLAKE: dataCenterId > maxDataCenterId; using random dataCenterId:{}", this.dataCenterId);
        }
        log.info("SNOWFLAKE: initialised with dataCenterId:{}, sequence:{}", this.dataCenterId, this.sequence);
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp
     *            上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    protected long getDataCenterId() throws SocketException, UnknownHostException {
        NetworkInterface network = null;

        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            NetworkInterface nint = en.nextElement();
            if (!nint.isLoopback() && nint.getHardwareAddress() != null) {
                network = nint;
                break;
            }
        }

        byte[] mac = network.getHardwareAddress();
        byte rndByte = (byte) (random.nextInt() & 0x000000FF);

        // take the last byte of the MAC address and a random byte as datacenter ID
        return ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) rndByte) << 8))) >> 6;
    }

    /**
     * Return the next unique id for the type with the given name using the generator's id generation strategy.
     *
     * @return
     */
    public synchronized long getId() {
        // 当前系统时间戳：毫秒
        long timestamp = System.currentTimeMillis();

        // 如果当前时间小于上一次ID生成时的时间戳，说明系统时钟回退过这个时候应当抛出异常
        // 此处采取激进策略：强制线程睡眠 如果是高并发情况下会在此处形成线程在getId方法上排队等待获取锁现象
        // 当发生时钟回拨时
        if (timestamp < lastTimestamp) {
            // 如果时钟回拨在可接受范围内, 等待即可
            long offset = lastTimestamp - timestamp;
            log.warn("Clock moved backwards. Refusing to generate id for {} milliseconds.", offset);
            // 睡（lastTimestamp - currentTimestamp）ms让其追上
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(offset));
            timestamp = System.currentTimeMillis();
            // 如果时间还小于当前时间，那么利用扩展字段加1
            // 或者是采用抛异常并上报
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                        lastTimestamp - timestamp));
            }
        }

        // 如果是同一时间生成的(同一毫秒内), 则进行毫秒内序列
        // 这种情况只有在极高并发的情况下才会出现: 当前线程和上一个线程 或者是同一个线程前后两次获取本对象实例的锁
        if (lastTimestamp == timestamp) {
            // sequence累加并用sequenceMask防止溢出
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出，超过4095则归0
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = ThreadLocalRandom.current().nextInt(2);
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        long id = ((timestamp - twepoch) << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | sequence;

        if (id < 0) {
            log.warn("ID is smaller than 0: {}", id);
        }

        return id;
    }
}
