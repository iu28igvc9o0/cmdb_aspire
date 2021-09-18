package com.aspire.mirror.composite.service.cmdb;

import com.aspire.mirror.composite.service.inspection.payload.CompAllocateIpConfigListReq;
import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.aspire.mirror.composite.service.inspection.payload.Dict;
import com.aspire.mirror.composite.service.inspection.payload.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.mirror.composite.service.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/4/16 10:53
 * 版本: v1.0
 */
@Api(value = "字典表维护")
@RequestMapping("${version}/cmdb/configDict")
public interface IConfigDictService {
	
	@GetMapping(value = "list")
	@ApiOperation(value = "查询字典表管理列表", notes = "查询字典表管理列表", tags = {"ConfigDict API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 500, message = "内部错误")})
	Result<Dict> getAllConfigDictData(@RequestParam(value = "pageNum",required = false) int pageNum,
									  @RequestParam(value = "startPageNum",required = false) int startPageNum,
									  @RequestParam(value = "pageSize",required = false) int pageSize,
									  @RequestParam(value = "pcode",required = false) String pcode,
									  @RequestParam(value = "dictCode",required = false) String dictCode,
									  @RequestParam(value = "dictNote",required = false) String dictNote,
									  @RequestParam(value = "colName",required = false) String colName);
											 
	@PostMapping("/add")
	@ApiOperation(value = "添加字典表数据", notes = "添加字典表数据", tags = {"ConfigDict API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 500, message = "内部错误")})
    String insertCfgDict(@RequestBody Dict dict);
	
	@DeleteMapping("/delete")
	@ApiOperation(value = "删除字典表数据", notes = "删除字典表数据", tags = {"ConfigDict API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 500, message = "内部错误")})
    String deleteCfgDictById(@RequestParam("dictId") String dictId);
	
	@PostMapping("/update")
	@ApiOperation(value = "更新字典表数据", notes = "更新字典表数据", tags = {"ConfigDict API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 500, message = "内部错误")})
    String updateCfgDict(@RequestBody Dict dict);
	
	 @GetMapping("/getById")
	 @ApiOperation(value = "根据id查询字典表数据", notes = "根据id查询字典表数据", tags = {"ConfigDict API"})
	 @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			 @ApiResponse(code = 500, message = "内部错误")})
    Dict getById (@RequestParam("dictId") String dictId);

	@GetMapping("/getByIds")
	@ApiOperation(value = "根据id查询字典表数据", notes = "根据id查询字典表数据", tags = {"ConfigDict API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 500, message = "内部错误")})
	List<Dict> getByIds(@RequestParam("ids") String dictIds);

	@GetMapping("/getAll")
	@ApiOperation(value = "查询所有字典表数据", notes = "查询所有字典表数据", tags = {"ConfigDict API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 500, message = "内部错误")})
    List<Dict> getAll(@RequestParam(value = "dictId",required = false) String dictId);
	
	/**
	 * 导出字典表数据(所有数据)
	 */
	@RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "导出字典表数据", notes = "导出字典表数据", tags = {"ConfigDict API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 500, message = "内部错误")})
	void exportDict(@RequestParam(value = "pcode",required = false) String pcode,
					@RequestParam(value = "dictCode",required = false) String dictCode,
					@RequestParam(value = "dictNote",required = false) String dictNote,
					@RequestParam(value = "colName",required = false) String colName);
	
	@GetMapping("/getDicts")
	@ApiOperation(value = "查询字典表数据", notes = "查询字典表数据", tags = {"ConfigDict API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 500, message = "内部错误")})
	List<ConfigDict> getDicts ();
    
    @GetMapping("/getDictsByType")
    @ApiOperation(value = "根据type和pid查询字典表数据", notes = "根据type和pid查询字典表数据", tags = {"ConfigDict API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ConfigDict> getDictsByType(@RequestParam("type") String type,
										   @RequestParam(value="pid",required = false) String pid,
										   @RequestParam(value="pValue",required = false) String pValue,
										   @RequestParam(value="pType",required = false) String pType);

	@GetMapping("/getTree/{col_name}")
	@ApiOperation(value = "根据type和pid查询字典表数据", notes = "查询字典表树", tags = {"ConfigDict API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 500, message = "内部错误")})
	List<ConfigDict> getTree(@PathVariable("col_name") String colName);

	@GetMapping("/getDistinctDictType")
	@ApiOperation(value = "获取字段表所有的字段类型", notes = "获取字段表所有的字段类型", tags = {"ConfigDict API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 500, message = "内部错误")})
	List<Map> getDistinctDictType();
}
