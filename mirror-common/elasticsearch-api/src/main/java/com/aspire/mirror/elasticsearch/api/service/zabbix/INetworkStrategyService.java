package com.aspire.mirror.elasticsearch.api.service.zabbix;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.elasticsearch.api.dto.NetworkFillWallDto;
import com.aspire.mirror.elasticsearch.api.dto.NetworkLoadBalancerDto;
import com.aspire.mirror.elasticsearch.api.dto.NetworkPublicNetDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: INetworkStrategyService
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/7/2716:07
 */
@RequestMapping("${version}/network")
public interface INetworkStrategyService {
    /**
     * 新增防火墙配置
     *
     * @param networkStrategyDtoList
     */
    @PostMapping("fillWall")
    void insertFillWall(@RequestBody List<NetworkFillWallDto> networkStrategyDtoList);

    /**
     * 查询防火墙列表
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("fillWall")
    PageResult<Map<String, Object>> queryFillWall(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "sourceZone", required = false)
                                                          String sourceZone,
                                                  @RequestParam(value = "destinationZone", required = false)
                                                          String destinationZone,
                                                  @RequestParam(value = "action", required = false) String
                                                          action,
                                                  @RequestParam(value = "destinationAddress", required =
                                                          false) String destinationAddress,
                                                  @RequestParam(value = "sourceAddress", required = false)
                                                          String sourceAddress,
                                                  @RequestParam(value = "servicePort", required = false)
                                                          String servicePort,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);

    /**
     * 查询导出的数据
     *
     * @param keyword
     * @return
     */
    @PostMapping("fillWall/export")
    List<Map<String, Object>> exportFillWall(@RequestParam(value = "keyword", required = false) String keyword);

    /**
     * @param networkStrategyDtoList
     */
    @PostMapping("loadBalancer")
    void insertLoadBalancer(@RequestBody List<NetworkLoadBalancerDto> networkStrategyDtoList);

    /**
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("loadBalancer")
    PageResult<NetworkLoadBalancerDto> queryLoadBalancer(@RequestParam(value = "keyword", required = false) String
                                                                 keyword,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") int
                                                                 pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "50") int
                                                                 pageSize);

    /**
     * @param keyword
     * @return
     */
    @PostMapping("loadBalancer/export")
    List<NetworkLoadBalancerDto> exportLoadBalancer(@RequestParam(value = "keyword", required = false) String keyword);

    /**
     * @param networkStrategyDtoList
     */
    @PostMapping("publicNet")
    void insertPublicNet(@RequestBody List<NetworkPublicNetDto> networkStrategyDtoList);

    /**
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("publicNet")
    PageResult<NetworkPublicNetDto> queryPublicNet(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "50") int pageSize);

    /**
     * @param keyword
     * @return
     */
    @PostMapping("publicNet/export")
    List<NetworkPublicNetDto> exportPublicNet(@RequestParam(value = "keyword", required = false) String keyword);
}
