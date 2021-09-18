package com.aspire.ums.cmdb.v2.index.web;

import com.aspire.ums.cmdb.index.ItCloudScreenCheckScoreAPI;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenCheckScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenCheckScoreController
 * @Description TODO
 * @Author luowenbo
 * @Date 2020/4/10 17:05
 * @Version 1.0
 */
@RestController
@Slf4j
public class ItCloudScreenCheckScoreController implements ItCloudScreenCheckScoreAPI {

    @Autowired
    private ItCloudScreenCheckScoreService screenCheckScoreService;

    @Override
    public Map<String, Object> getCheckScoreList(@RequestBody ItCloudScreenRequest req) {
        return screenCheckScoreService.getCheckScoreList(req);
    }
}
