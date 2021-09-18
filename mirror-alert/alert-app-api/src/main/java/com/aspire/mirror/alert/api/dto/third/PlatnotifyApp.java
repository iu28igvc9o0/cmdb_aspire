package com.aspire.mirror.alert.api.dto.third;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class PlatnotifyApp {
	
	    private String level;      //INFO WARN ERROR DEFAULT(所有级别) ：低中高

	    private String[] subjects;
	    
	    @JsonProperty("push_history")
	    private boolean push_history = false;
	    @JsonProperty("app_name")
	    private String app_name;

	    private String url;  
	    
	    @JsonProperty("auth_type")
	    private String auth_type; //NONE BASIC
	    
	    
	    @JsonProperty("auth_user")
	    private String auth_user;   
	    
	    
	    
	    @JsonProperty("auth_pwd")
	    private String auth_pwd;    
}
