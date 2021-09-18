package com.migu.tsg.microservice.atomicservice.composite.service.monitor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload.MetricCatalogResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface ICompMetricCatalogService {
    
    /**
     * Get scheme of metrics supported.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "Get scheme of metrics supported", notes = "Get scheme of metrics supported",
           response = MetricCatalogResponse.class, tags = {"Composite Metric Catalog service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Get scheme of metrics supported",
                                        response = MetricCatalogResponse.class)})
    @GetMapping(path = "/v1/metrics-catalog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    MetricCatalogResponse getMetricCatalog();
}
