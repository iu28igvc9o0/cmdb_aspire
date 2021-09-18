package com.aspire.mirror.composite.payload.notify;

import lombok.Data;

@Data
public class SmsListParamReq {
	private String startTime;
	private String endTime;
	private String receiver;
    private String content;
    private Integer status;
    private Integer pageNo;
    private Integer pageSize;
    
    /**
startTime	否	string	开始时间
endTime	否	string	结束时间
receiver	否	string	手机号码/姓名
content	否	string	短信内容
status	否	integer	状态 1成功 0失败
pageNo	是	integer	当前页
pageSize	是	integer	每页大小
     * 
     */
}
