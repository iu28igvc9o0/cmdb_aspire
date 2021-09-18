package com.migu.tsg.microservice.atomicservice.composite.service.rbac;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.BulkActionsRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.BulkActionsResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserViewAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Retrieving specific permissions endpoint
 * Project Name:composite-api
 * File Name:ICompositePermission.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.service
 * ClassName: ICompositePermission <br/>
 * date: 2017年8月27日 下午6:01:19 <br/>
 * Retrieving specific permissions endpoint
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Api(value = "${version}/Permissions", description = "Retrieving the actions of the resources.")
public interface ICompPermissionService {

    /**
     * 作者：yangshilei
     * 展示用户有view权限的资源情况
     *
     * @param namespace
     * @param username
     * @return
     */
    @ApiOperation(value = "用户拥有的资源view权限展示", notes = "用户拥有的资源view权限展示",
            response = UserViewAction.class, tags = {"Permissions API"})
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "User owned resource view permissions display", response = UserViewAction.class)})
    @RequestMapping(value = "${version}/permissions/{namespace}/{username}/actions",
            method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    List<String> listUserActions(@PathVariable("namespace") final String namespace,
                                 @PathVariable("username") final String username,
                                 @RequestParam(value = "is_admin", defaultValue = "false") final Boolean isAdmin);


    /**
     * 获取用户指定资源类型的操作集合. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param namespace
     * @param resourceType
     * @return 用户对指定资源类型的操作集合    如   [service:create, service:update]
     */
    @ApiOperation(value = "获取指定资源类型的操作集合", notes = "获取指定资源类型的操作集合",
            response = String.class, tags = {"Permissions API"})
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Returns the allowed actions for the scope", response = String.class)})
    @RequestMapping(value = "${version}/permissions/{namespace}/{resource_type}",
            method = RequestMethod.GET, produces = "application/json")
    List<String> actions(@PathVariable("namespace") String namespace,
                         @PathVariable("resource_type") String resourceType);

    /**
     * 对给定的资源列表，过滤并填充操作集合.
     * <p>
     * 作者： yangshilei
     *
     * @param rbacResList
     * @param namespace
     * @return
     */
    @ApiOperation(value = "批量过滤并填充给定资源的操作", notes = "批量过滤并填充给定资源的操作",
            response = BulkActionsResponse.class, tags = {"Permissions API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "return filtered resources",
            response = BulkActionsResponse.class)})
    @RequestMapping(value = "${version}/permissions/{namespace}/{resource_type}",
            method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    List<BulkActionsResponse> bulkActions(@RequestBody List<BulkActionsRequest> resList,
                                          @PathVariable("namespace") String namespace, @PathVariable("resource_type")
                                                  String resourceType,
                                          @RequestParam(name = "action", required = false, defaultValue = "view")
                                                  String action);

    @RequestMapping(value = "${version}/permissions/getAuthDeviceData",
            method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    List<Map<String, String>> getAuthDeviceData(@RequestParam(name = "area") String area, @RequestParam(name =
            "bizSystem", required = false) String
            bizSystem, @RequestParam(name = "deviceType", required = false) String deviceType);

}
