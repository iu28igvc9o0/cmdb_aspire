package com.aspire.mirror.indexproxy.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    ExecuteUtil
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/6 10:17
 * 版本:      v1.0
 */
public class ExecuteUtil {

    public static <T> void partitionRun(List<T> dataList, int size, Consumer<Pair<String, List<T>>> consumer) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        Preconditions.checkArgument(size > 0, "size must not be a  ");
        List<List<T>> partitionList = Lists.partition(dataList, size);
        for (int i = 0; i < partitionList.size(); i++) {
            if (i == 0) {
                consumer.accept(Pair.of("START", partitionList.get(i)));
            } else if (i == partitionList.size() - 1) {
                consumer.accept(Pair.of("END", partitionList.get(i)));
            } else {
                consumer.accept(Pair.of("MID", partitionList.get(i)));
            }
        }
    }

    public static <T, V> List<V> partitionCall2List(List<T> dataList, int size, Function<List<T>, List<V>> function) {

        if (CollectionUtils.isEmpty(dataList)) {
            return new ArrayList<>(0);
        }
        Preconditions.checkArgument(size > 0, "size must not be a minus");

        return Lists.partition(dataList, size)
                .stream()
                .map(function)
                .filter(Objects::nonNull)
                .reduce(new ArrayList<>(),
                        (resultList1, resultList2) -> {
                            resultList1.addAll(resultList2);
                            return resultList1;
                        });


    }

    public static <T, V> Map<T, V> partitionCall2Map(List<T> dataList, int size, Function<List<T>, Map<T, V>> function) {
        if (CollectionUtils.isEmpty(dataList)) {
            return new HashMap<>(0);
        }
        Preconditions.checkArgument(size > 0, "size must not be a minus");
        return Lists.partition(dataList, size)
                .stream()
                .map(function)
                .filter(Objects::nonNull)
                .reduce(new HashMap<>(),
                        (resultMap1, resultMap2) -> {
                            resultMap1.putAll(resultMap2);
                            return resultMap1;
                        });


    }
}
