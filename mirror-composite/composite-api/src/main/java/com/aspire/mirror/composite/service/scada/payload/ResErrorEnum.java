package com.aspire.mirror.composite.service.scada.payload;

/**
 * 
 * @ClassName: ResultEnum
 * @Description:返回结果枚举类
 * @author: JinSu
 * @date: 2018年3月19日 下午4:07:39
 * 
 * @Copyright: 2018 http://www.ttfs-edu.com
 *             注意：本内容仅限于深圳市誉天智骏教育科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum ResErrorEnum {

	// 公共返回结果
	SUCCESS(200, "请求成功"),
	ERROR(500, "服务器错误,请联管理员小哥哥~"),
	NULL(501, "返回空值"),
	NOTICE(300, "提示信息"),
	WARN(301, "警告信息"),
	BUSINESSNOTICE(305,"业务异常"),
	RESOURCEEXIST(500,"资源已存在");




	private Integer code;
	private String msg;

	private ResErrorEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
