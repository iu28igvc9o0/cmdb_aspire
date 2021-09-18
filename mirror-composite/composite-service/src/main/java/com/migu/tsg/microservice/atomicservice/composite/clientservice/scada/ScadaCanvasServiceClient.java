package com.migu.tsg.microservice.atomicservice.composite.clientservice.scada;

import com.aspire.mirror.scada.api.service.ScadaCanvasService;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("SCADA-SERVICE")
public interface ScadaCanvasServiceClient extends ScadaCanvasService {
}
