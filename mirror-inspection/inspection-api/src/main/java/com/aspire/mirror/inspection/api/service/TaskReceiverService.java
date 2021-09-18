//package com.aspire.mirror.inspection.api.service ;
//
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import com.aspire.mirror.inspection.api.dto.vo.TaskReceiverVO;
//import com.aspire.mirror.inspection.api.dto.TaskReceiverCreateRequest;
//import com.aspire.mirror.inspection.api.dto.TaskReceiverCreateResponse;
//import com.aspire.mirror.inspection.api.dto.TaskReceiverUpdateRequest;
//import com.aspire.mirror.inspection.api.dto.TaskReceiverUpdateResponse;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//
//import java.util.List;
//
///**
// * inspection_task_receiver对外暴露接口
// *
// * 项目名称:  微服务运维平台
// * 包:       com.aspire.mirror.inspection.api.service
// * 类名称:    TaskReceiverService.java
// * 类描述:    inspection_task_receiver对外暴露接口层
// * 创建人:    ZhangSheng
// * 创建时间:  2018-07-27 13:48:08
// */
//@Api(value = "inspection_task_receiver")
//public interface TaskReceiverService{
//    /**
//     * 创建inspection_task_receiver信息
//     *
//     * @param taskReceiverCreateRequest inspection_task_receiver创建请求对象
//     * @return TaskReceiverCreateResponse inspection_task_receiver创建响应对象
//     */
//    @PostMapping(value="/v1/inspection_task_receiver/insert", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value="创建inspection_task_receiver",notes="创建inspection_task_receiver",response=TaskReceiverCreateResponse.class,tags={ "/v1/inspection_task_receiver" })
//    @ApiResponses(value = {
//    @ApiResponse(code = 200, message = "返回", response =  TaskReceiverCreateResponse.class),
//    @ApiResponse(code = 500, message = "Unexpected error", response = TaskReceiverCreateResponse.class) })
//    TaskReceiverCreateResponse createdTaskReceiver(@RequestBody TaskReceiverCreateRequest taskReceiverCreateRequest);
//    /**
//     * 根据主键删除单条inspection_task_receiver信息
//     *
//     * @param receiverId 主键
//     * @@return Result 返回结果
//     */
//    @DeleteMapping(value = "/v1/inspection_task_receiver/{inspection_task_receiver}")
//    @ApiOperation(value = "删除单条inspection_task_receiver信息", notes = "删除单条inspection_task_receiver信息",tags = {"/v1/inspection_task_receiver"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
//    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("receiver_id") String receiverId);
//
//    /**
//     * 根据主键删除多条inspection_task_receiver信息
//     *
//     * @param receiverIds 主键（以逗号分隔）
//     * @@return Result 返回结果
//     */
//    @DeleteMapping(value = "/v1/inspection_task_receiver/{receiver_id}")
//    @ApiOperation(value = "删除多条inspection_task_receiver信息", notes = "删除多条inspection_task_receiver信息",tags = {"/v1/inspection_task_receiver"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
//    ResponseEntity<String> deleteByPrimaryKeyArrays(@PathVariable("receiver_ids") String receiverIds);
//
//    /**
//     * 根据主键修改inspection_task_receiver信息
//     * @param taskReceiverUpdateRequest inspection_task_receiver修改请求对象
//     * @return TaskReceiverUpdateResponse inspection_task_receiver修改响应对象
//     */
//    @PutMapping(value="/v1/inspection_task_receiver/{receiver_id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value="修改inspection_task_receiver",notes="修改inspection_task_receiver",response=TaskReceiverUpdateResponse.class,tags={ "/v1/inspection_task_receiver" })
//    @ApiResponses(value = {
//    @ApiResponse(code = 200, message = "返回", response =  TaskReceiverUpdateResponse.class),
//    @ApiResponse(code = 500, message = "Unexpected error", response = TaskReceiverUpdateResponse.class) })
//    TaskReceiverUpdateResponse modifyByPrimaryKey(@PathVariable("receiver_id") String receiverId,@RequestBody TaskReceiverUpdateRequest taskReceiverUpdateRequest);
//
//    /**
//     * 根据主键查找inspection_task_receiver详情信息
//     * @param receiverId inspection_task_receiver主键
//     * @return TaskReceiverVO inspection_task_receiver详情响应对象
//     */
//    @GetMapping(value = "/v1/inspection_task_receiver/{receiver_id}")
//    @ApiOperation(value = "详情", notes = "根据receiverId获取inspection_task_receiver详情", tags = {"/v1/inspection_task_receiver"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = TaskReceiverVO.class),
//    @ApiResponse(code = 500, message = "内部错误")})
//    TaskReceiverVO findByPrimaryKey(@PathVariable("receiver_id") String receiverId);
//
//    /**
//     * 根据主键查询inspection_task_receiver集合信息
//     *
//     * @param receiverIds inspection_task_receiver主键(多个以逗号分隔)
//     * @return List<TaskReceiverVO> inspection_task_receiver查询响应对象
//     */
//   /* @GetMapping(value = "/v1/inspection_task_receiver/{receiver_id}")
//    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/inspection_task_receiver"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
//    @ApiResponse(code = 500, message = "内部错误")})
//    List<TaskReceiverVO> listByPrimaryKeyArrays(@PathVariable("receiver_id") String receiverIds);*/
//
//}
