package com.migu.tsg.microservice.atomicservice.composite.helper;

import com.migu.tsg.microservice.atomicservice.composite.biz.AlertBiz;
import com.migu.tsg.microservice.atomicservice.composite.biz.OpsBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author baiwenping
 * @Title: OpsHelper
 * @Package com.migu.tsg.microservice.atomicservice.composite.helper
 * @Description: TODO
 * @date 2021/3/16 17:59
 */
@Component
public class OpsHelper {
    @Autowired
    private List<OpsBiz> opsBizList;

    public List<String> queryAllChildGroup(List<String> groupIdList) {
        for (OpsBiz opsBiz: opsBizList) {
            if (!opsBiz.isBasic()) {
                return opsBiz.queryAllChildGroup(groupIdList);
            }
        }
        return opsBizList.get(0).queryAllChildGroup(groupIdList);
    }
}
