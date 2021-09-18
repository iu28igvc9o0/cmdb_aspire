package com.migu.tsg.microservice.atomicservice.composite.service.rbac;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserPayload;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserPicturePayload;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserQueryPagePayload;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.entity
 * 类名称:     User
 * 类描述:     对外暴露接口层
 * 创建人:     曾祥华
 * 创建时间:     2019-03-07 16:04:48
 */

@RequestMapping(value = "${version}/user", produces = "application/json;charset=UTF-8")
@Api(value = "Composite user service", description = "Composite user service")
public interface IUserService {
    /**
     * 创建信息
     *
     * @param UserPayload 动作创建请求对象
     * @return UserCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建", notes = "创建", response = UserPayload.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = UserPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = UserPayload.class)})
    UserPayload createdUser(@RequestBody UserPayload userPayload, @RequestParam(name = "picture", required = false)
            MultipartFile picture);

    /**
     * 根据主键删除单作信息
     *
     * @param actionId 主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/{user_id}")
    @ApiOperation(value = "删除单条信息", notes = "删除单条信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("user_id") String userId);

    @PostMapping("/importExcel")
    @ApiOperation(value = "excel导入人员", notes = "excel导入人员")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> excelImport(@RequestParam("file") MultipartFile file);

    /**
     * 根据主键修改信息
     *
     * @param UserUpdateRequest 修改请求对象
     * @return UserUpdateResponse 修改响应对象
     */
    @PostMapping(value = "/update/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改", notes = "修改", response = UserPayload.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = UserPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = UserPayload.class)})
    UserPayload modifyByPrimaryKey(@PathVariable(name = "user_id", required = false) String userId, @RequestBody
            UserPayload userUpdateRequest, @RequestParam(name = "picture", required = false) MultipartFile picture);

    /**
     * 根据主键查作详情信息
     *
     * @param userId 动作主键
     * @return UserVO 动作详情响应对象
     */
    @GetMapping(value = "/{user_id}")
    @ApiOperation(value = "详情", notes = "根据userId获取动作详情")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = UserPayload.class),
            @ApiResponse(code = 500, message = "内部错误")})
    UserPicturePayload findByPrimaryKey(@PathVariable("user_id") String userId);

    /**
     * 根据主键查询动作集合信息
     *
     * @param userId 动作主键
     * @return UserVO 动作详情响应对象
     */
    @GetMapping(value = "/list/{user_id}")
    @ApiOperation(value = "查询", notes = "查询")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<UserPayload> listByPrimaryKeyArrays(@PathVariable("user_id") String userIds);

    /**
     * 创建信息
     *
     * @param UserPayload 动作创建请求对象
     * @return UserCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/queryList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询列表", notes = "查询列表", response = UserPayload.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = UserPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = UserPayload.class)})
    List<UserResponse> queryList(@RequestBody UserPayload userPayload);

    /**
     * 根据page对象查询监控项
     *
     * @param request 分页查询监控对象
     * @return PageResult<UserResponse> page返回对象
     */
    @PostMapping(value = "/pageList")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误")})
    PageResult<UserResponse> pageList(@RequestBody UserQueryPagePayload userPayload);

    /**
     * 创建信息
     *
     * @param UserPayload 动作创建请求对象
     * @return UserCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/exportExcel")
    @ApiOperation(value = "excel导出人员", notes = "excel导出人员")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void excelExport(@RequestBody UserPayload userPayload, HttpServletResponse response);

    /**
     * 创建信息
     *
     * @param UserPayload 动作创建请求对象
     * @return UserCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/excel/import/template")
    @ApiOperation(value = "excel导入人员模板下载", notes = "excel导入人员模板下载")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    void importTemplate(HttpServletResponse response);

    @PutMapping(value = "/batchModifyDeptId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "批量修改部门ID", notes = "批量修改部门ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    int batchModifyDeptIdByUserIdArrays(@RequestParam("dept_id") String deptId, @RequestParam("user_ids") String
            userIds);

    /**
     * 根据ldapID查询用户详情
     *
     * @param userId 动作主键
     * @return UserVO 动作详情响应对象
     */
    @GetMapping(value = "/findByLdapId/{ldap_id}")
    @ApiOperation(value = "详情", notes = "根据ldap用户标识获取用户信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    UserResponse findByLdapId(@RequestParam("ldapId") String ldapId);

    @RequestMapping(value = "/sendSmsCaptcha", method = RequestMethod.POST, produces =
            "application/json; charset=UTF-8")
    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    BaseResponse sendSmsCaptcha(@RequestParam(name = "namespace") String namespace, @RequestParam(name = "username") String
            username, @RequestParam(name = "mobile") String mobile, HttpServletRequest request);

    @PutMapping(value = "/validSmsCaptcha", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "校验短信验证码", notes = "校验短信验证码")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error")})
    BaseResponse validSmsCaptcha(@RequestParam(name = "mobile") String mobile, @RequestParam(name = "validCode")
            String validCode, HttpServletRequest request);
}
