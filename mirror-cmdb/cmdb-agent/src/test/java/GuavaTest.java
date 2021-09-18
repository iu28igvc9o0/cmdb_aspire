import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.Test;

import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/12/29 17:10
 */
@Slf4j
public class GuavaTest {

    @Test
    public void testStopWatch() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        TimeUnit.MILLISECONDS.sleep(1000);
        stopwatch.stop();
        log.info("获取待更新的资产存活列表耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset().start();
        TimeUnit.MILLISECONDS.sleep(3000);
        log.info("更新资产的存活状态为“已存活”,耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        stopwatch.reset().start();
        TimeUnit.MILLISECONDS.sleep(4000);
        log.info("保存“已存活”到存活状态检测表,耗时:[{}]ms.", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testGroup() {
        List<Map<String, String>> mapList = Lists.newArrayList();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Map<String, String> map = Maps.newHashMap();
            map.put("id", UUIDUtil.getUUID());
            map.put("latestSurvivalTime", "2020-12-29 12:23:55");
            mapList.add(map);
        });

        IntStream.rangeClosed(1, 5).forEach(i -> {
            Map<String, String> map = Maps.newHashMap();
            map.put("id", UUIDUtil.getUUID());
            map.put("latestSurvivalTime", "2020-12-29 14:55:41");
            mapList.add(map);
        });
        IntStream.rangeClosed(1, 23).forEach(i -> {
            Map<String, String> map = Maps.newHashMap();
            map.put("id", UUIDUtil.getUUID());
            map.put("latestSurvivalTime", "2020-12-29 20:10:45");
            mapList.add(map);
        });

        Map<String, List<String>> paramMap = Maps.newHashMap();
        for (Map<String, String> map : mapList) {
            String instanceId = map.get("id");
            String latestSurvivalTime = map.get("latestSurvivalTime");
            if (org.apache.commons.lang3.StringUtils.isBlank(latestSurvivalTime)) {
                continue;
            }
            if (paramMap.get(latestSurvivalTime) == null) {
                List<String> instanceIdList = Lists.newArrayList();
                paramMap.put(latestSurvivalTime, instanceIdList);
            } else {
                paramMap.get(latestSurvivalTime).add(instanceId);
            }
        }
        log.info("paramMap={}", paramMap);
    }
}
