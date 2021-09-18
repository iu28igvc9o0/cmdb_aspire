package com.aspire.mirror.elasticsearch.api.service.network;

import com.aspire.mirror.elasticsearch.api.dto.network.NetworkIndicatorRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/4 21:03
 */
@RequestMapping(value = "/v1/alerts/netWork")
@Service(value = "esNetworkIndicator")
public interface IEsNetworkIndicatorService {

    /**
     * 网络端口设备列表查询
     *
     * @param request
     * @return List<Map < String, String>>
     */
    @PostMapping(value = "/queryNetworkPortDeviceList")
    @ApiOperation(value = "网络端口设备列表查询", notes = "网络端口设备列表查询", response = ResponseEntity.class, tags = {"Alert_Network_Port_API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    List<Map<String, String>> queryNetworkPortDeviceList(@RequestBody NetworkIndicatorRequest request);


    /**
     * 数据包详情查询
     *
     * @param idcType
     * @param roomId
     * @param item
     * @param host
     * @param port
     * @param resourceId
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param pageNum
     * @return List<Map < String, String>>
     */
    @GetMapping(value = "/queryDataPageDetail")
    @ApiOperation(value = "数据包详情查询", notes = "数据包详情查询", response = ResponseEntity.class, tags = {"Alert_Network_Port_API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    List<Map<String, Object>> queryDataPageDetail(@RequestParam(value = "idcType", required = false) String idcType,
                                                  @RequestParam(value = "roomId", required = false) String roomId,
                                                  @RequestParam(value = "item", required = false) String item,
                                                  @RequestParam(value = "host", required = false) String host,
                                                  @RequestParam(value = "port", required = false) String port,
                                                  @RequestParam(value = "resourceId", required = false) String resourceId,
                                                  @RequestParam(value = "startTime", required = false) String startTime,
                                                  @RequestParam(value = "endTime", required = false) String endTime,
                                                  @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum


    );

    /**
     * 数据包详情导出查询
     *
     * @param idcType
     * @param roomId
     * @param item
     * @param host
     * @param port
     * @param resourceId
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param pageNum
     * @return List<Map < String, String>>
     */
    @GetMapping(value = "/queryExportDataPageDetail")
    @ApiOperation(value = "数据包详情导出查询", notes = "数据包详情导出查询", response = ResponseEntity.class, tags = {"Alert_Network_Port_API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
    List<Map<String, Object>> queryExportDataPageDetail(@RequestParam(value = "idcType", required = false) String idcType,
                                                        @RequestParam(value = "roomId", required = false) String roomId,
                                                        @RequestParam(value = "item", required = false) String item,
                                                        @RequestParam(value = "host", required = false) String host,
                                                        @RequestParam(value = "port", required = false) String port,
                                                        @RequestParam(value = "resourceId", required = false) String resourceId,
                                                        @RequestParam(value = "startTime", required = false) String startTime,
                                                        @RequestParam(value = "endTime", required = false) String endTime,
                                                        @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum


    );


}
