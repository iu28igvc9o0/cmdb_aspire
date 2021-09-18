package com.aspire.mirror.bean;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class Result implements Serializable{
	
    /**
    * serialVersionUID:TODO(用一句话描述这个变量表示什么).
    * @since JDK 1.6
    */
        
    private static final long serialVersionUID = 9085085005467879389L;
    private String resultCode;
	private String resultDes;

	private static final String SUCCESS_CODE = "200";
	private static final String SUCCESS_DESC = "success";

	private static final Result SUCCESS_INSTANCE = new Result(SUCCESS_CODE, SUCCESS_DESC);

	public Result(String resultCode, String resultDes) {
		this.resultCode = resultCode;
		this.resultDes = resultDes;
	}

	public static Result success() {
		return SUCCESS_INSTANCE;
	}
}